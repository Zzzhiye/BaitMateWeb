package baitmate.Controller;

import baitmate.DTO.*;
import baitmate.Service.TokenService;
import baitmate.Service.UserService;
import baitmate.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
  @Autowired private UserService userService;

  @Autowired private TokenService tokenService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    try {
      User user = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());
      String token = tokenService.generateToken(user);
      LoginResponse loginResponse = new LoginResponse(user.getId(), token);
      System.out.println(loginResponse);
      return ResponseEntity.ok(loginResponse);
    } catch (IllegalArgumentException ex) {
      LoginResponse errorResponse = new LoginResponse(ex.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .contentType(MediaType.APPLICATION_JSON)
          .body(errorResponse);
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
    System.out.println("Received Token: " + token); // Debugging

    if (token == null || token.isEmpty()) {
      return ResponseEntity.badRequest().body("Token is missing");
    }

    boolean deactivated = tokenService.deactivateToken(token);
    if (deactivated) {
      return ResponseEntity.ok("Logged out successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Token not found or already invalidated");
    }
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
    Optional<User> userOptional = userService.findByUsername(request.getUsername());
    if (userOptional.isEmpty()) {
      System.out.println("User with username " + request.getUsername() + " not found.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    User user = userOptional.get();
    // System.out.println("Checking if email " + request.getEmail() + " matches the user's email.");

    if (!user.getEmail().equalsIgnoreCase(request.getEmail())) {
      // System.out.println("Email does not match for username: " + request.getUsername() +".
      // Provided: " + request.getEmail() + ". Stored: " + user.getEmail());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email does not match");
    }

    try {
      String otp = userService.generateAndSendOTP(request.getEmail());
      System.out.println("Generating and sending OTP to email: " + request.getEmail());
      return ResponseEntity.ok("OTP sent successfully to " + request.getEmail());
    } catch (IllegalArgumentException ex) {
      System.out.println(
          "Failed to send OTP for email: " + request.getEmail() + ". Error: " + ex.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
    if (userService.verifyOTP(request.getEmail(), request.getOtp())) {
      userService.updatePassword(request.getEmail(), request.getNewPassword());
      return ResponseEntity.ok("Password reset successfully");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
    }
  }

  @GetMapping("/validate-token")
  public ResponseEntity<?> validateToken(@RequestParam String token) {
    boolean isValid = tokenService.validateToken(token);

    if (isValid) {
      return ResponseEntity.ok(Collections.singletonMap("message", "Token is valid"));
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Collections.singletonMap("message", "Invalid or expired token"));
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
    if (userService.existsByUsername(registerRequest.getUsername())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
    }
    try {
      System.out.println(registerRequest.getUsername());

      User newUser = userService.registerUser(registerRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    } catch (ConstraintViolationException ex) {
      // Extract and return the validation messages
      List<String> errors =
          ex.getConstraintViolations().stream()
              .map(ConstraintViolation::getMessage)
              .collect(Collectors.toList());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .contentType(MediaType.APPLICATION_JSON)
          .body(errors);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
  }

  @PostMapping("/user/{userId}/follow")
  public ResponseEntity<?> followUser(@PathVariable long userId, @RequestParam long targetUserId) {
    try {
      userService.followUser(userId, targetUserId);
      return ResponseEntity.ok(userId + " successfully followed " + targetUserId);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
  }

  @PostMapping("/user/{userId}/unfollow")
  public ResponseEntity<?> unfollowUser(
      @PathVariable long userId, @RequestParam long targetUserId) {
    try {
      userService.unfollowUser(userId, targetUserId);
      return ResponseEntity.ok(userId + " successfully unfollowed " + targetUserId);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
  }

  @GetMapping("/user/{userId}/followers")
  public ResponseEntity<?> getFollowers(@PathVariable long userId) {
    try {
      List<User> followers = userService.findFollowers(userId);
      return ResponseEntity.ok(followers);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
  }

  @GetMapping("/user/{userId}/following")
  public ResponseEntity<?> getFollowing(@PathVariable long userId) {
    try {
      List<User> following = userService.findFollowing(userId);
      return ResponseEntity.ok(following);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<?> getUserDetails(@PathVariable long userId) {
    try {
      User user = userService.searchByUserId(userId);
      return ResponseEntity.ok(user);
    } catch (NullPointerException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
  }

  @GetMapping("/api/users")
  @ResponseBody
  public ResponseEntity<?> getUsersApi() {
    try {
      List<User> users = userService.getAllUsers();
      if (users.isEmpty()) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(users);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error retrieving users: " + e.getMessage());
    }
  }
}
