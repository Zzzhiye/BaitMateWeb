package baitmate.Controller;

import baitmate.DTO.ForgotPasswordRequest;
import baitmate.DTO.ResetPasswordRequest;
import baitmate.Service.AdminService;
import baitmate.Service.EmailService;
import baitmate.model.Admin;
import jakarta.servlet.http.HttpSession;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // Use @Controller
@RequestMapping("/api/admin")
public class AdminPasswordController {

  private static final Logger logger = LoggerFactory.getLogger(AdminPasswordController.class);

  @Autowired private AdminService adminService;

  @Autowired private EmailService emailService;

  @GetMapping("/login")
  public String showLoginPage() {
    return "login";
  }

  @PostMapping("/forgot-password")
  @ResponseBody
  public ResponseEntity<?> adminForgotPassword(
      @RequestBody ForgotPasswordRequest request, HttpSession session) {
    logger.info("Received forgot password request for admin: {}", request.getUsername());

    // Input validation
    if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
      return ResponseEntity.badRequest()
          .body("<p style='color:red;'>Username cannot be empty.</p>");
    }
    if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
      return ResponseEntity.badRequest().body("<p style='color:red;'>Email cannot be empty.</p>");
    }

    // Retrieve admin by username
    Admin admin = adminService.searchUserByUserName(request.getUsername());
    if (admin == null) {
      logger.warn("Admin not found: {}", request.getUsername());
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("<p style='color:red;'>Admin not found</p>");
    }

    // Email validation: Check if entered email matches the admin's actual email
    if (!admin.getEmail().equalsIgnoreCase(request.getEmail())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("<p style='color:red;'>Username and email do not match.</p>");
    }

    // Generate OTP
    String adminOTP = String.format("%06d", new Random().nextInt(999999));
    logger.info("Generated Admin OTP: {}", adminOTP);

    // Store OTP and email in the session
    session.setAttribute("otp", adminOTP);
    session.setAttribute("email", admin.getEmail()); // Store the *correct* email

    try {
      // Send OTP email
      emailService.sendSimpleMessage(
          admin.getEmail(), // Send to the *correct* email
          "Admin Password Reset OTP",
          "Your OTP for password reset is: " + adminOTP);
      return ResponseEntity.ok(
          "<p style='color:green;'>OTP sent successfully to " + admin.getEmail() + "</p>");
    } catch (Exception ex) {
      logger.error("Failed to send OTP email", ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("<p style='color:red;'>Failed to send OTP email: " + ex.getMessage() + "</p>");
    }
  }

  @PostMapping("/validate-otp")
  @ResponseBody
  public ResponseEntity<?> validateOTP(@RequestParam String otp, HttpSession session) {
    if (otp == null || otp.trim().isEmpty()) {
      return ResponseEntity.badRequest().body("<p style='color:red;'>OTP cannot be empty.</p>");
    }

    // Retrieve OTP from session
    String storedOtp = (String) session.getAttribute("otp");

    if (storedOtp != null && storedOtp.equals(otp)) {
      // OTP is valid.  We *don't* clear the session here. We need the email in the next step.
      return ResponseEntity.ok("<p style='color:green;'>OTP validated</p>");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("<p style='color:red;'>Invalid OTP</p>");
    }
  }

  @PostMapping("/reset-password")
  @ResponseBody
  public ResponseEntity<?> resetPassword(
      @RequestBody ResetPasswordRequest request, HttpSession session) {

    // Retrieve email from session.  This is now the ONLY place we get the email.
    String email = (String) session.getAttribute("email");

    if (email == null) {
      // If email is not in the session, it means the user didn't go through the forgot password
      // flow.
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("<p style='color:red;'>Session expired or invalid request.</p>");
    }

    // Input validation for the new password.
    if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
      return ResponseEntity.badRequest()
          .body("<p style='color:red;'>New password cannot be empty.</p>");
    }

    try {
      logger.info("Updating password for email: {}", email); // Log the retrieved email
      logger.info("New password (before encoding): {}", request.getNewPassword());
      adminService.updatePassword(email, request.getNewPassword()); // Use email from session

      // Clear session after successful password reset
      session.invalidate();
      return ResponseEntity.ok("<p style='color:green;'>Password reset successfully</p>");

    } catch (Exception ex) {
      logger.error("Failed to reset password", ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("<p style='color:red;'>Failed to reset password: " + ex.getMessage() + "</p>");
    }
  }

  @GetMapping("/test-email") // Create temporary endpoint for test only
  public ResponseEntity<?> testEmail() {
    try {
      emailService.sendSimpleMessage(
          "your_recipient_email@example.com", "Test Email", "This is a test email.");
      return ResponseEntity.ok("Test email sent!");
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to send test email: " + ex.getMessage());
    }
  }
}