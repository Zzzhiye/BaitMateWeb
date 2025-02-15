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

@Controller 
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

    
    if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
      return ResponseEntity.badRequest()
          .body("<p style='color:red;'>Username cannot be empty.</p>");
    }
    if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
      return ResponseEntity.badRequest().body("<p style='color:red;'>Email cannot be empty.</p>");
    }

    
    Admin admin = adminService.searchUserByUserName(request.getUsername());
    if (admin == null) {
      logger.warn("Admin not found: {}", request.getUsername());
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("<p style='color:red;'>Admin not found</p>");
    }

    
    if (!admin.getEmail().equalsIgnoreCase(request.getEmail())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("<p style='color:red;'>Username and email do not match.</p>");
    }

    
    String adminOTP = String.format("%06d", new Random().nextInt(999999));
    logger.info("Generated Admin OTP: {}", adminOTP);

    
    session.setAttribute("otp", adminOTP);
    session.setAttribute("email", admin.getEmail()); 

    try {
      
      emailService.sendSimpleMessage(
          admin.getEmail(), 
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

    
    String storedOtp = (String) session.getAttribute("otp");

    if (storedOtp != null && storedOtp.equals(otp)) {
      
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

    
    String email = (String) session.getAttribute("email");

    if (email == null) {
      
      
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("<p style='color:red;'>Session expired or invalid request.</p>");
    }

    
    if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
      return ResponseEntity.badRequest()
          .body("<p style='color:red;'>New password cannot be empty.</p>");
    }

    try {
      logger.info("Updating password for email: {}", email); 
      logger.info("New password (before encoding): {}", request.getNewPassword());
      adminService.updatePassword(email, request.getNewPassword()); 

      
      session.invalidate();
      return ResponseEntity.ok("<p style='color:green;'>Password reset successfully</p>");

    } catch (Exception ex) {
      logger.error("Failed to reset password", ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("<p style='color:red;'>Failed to reset password: " + ex.getMessage() + "</p>");
    }
  }

  @GetMapping("/test-email") 
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