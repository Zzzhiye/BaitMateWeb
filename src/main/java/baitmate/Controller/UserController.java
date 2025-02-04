package baitmate.Controller;

import baitmate.DTO.*;
import baitmate.Service.TokenService;
import baitmate.Service.UserService;
import baitmate.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    private static final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;
    private static final Key SECRET_KEY = ALGORITHM.key().build();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.validateUser(loginRequest.getUsername(),loginRequest.getPassword());
            String token = tokenService.generateToken(user);
            LoginResponse loginResponse = new LoginResponse(user, token);
            return ResponseEntity.ok(loginResponse);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(ex.getMessage()));
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found or already invalidated");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Optional<User> userOptional = userService.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOptional.get();
        String email = user.getEmail();

        try {
            String otp = userService.generateAndSendOTP(email);
            return ResponseEntity.ok("OTP sent successfully to " + email);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (userService.verifyOTP(request.getUsername(), request.getOtp())) {
            userService.updatePassword(request.getUsername(), request.getNewPassword());
            return ResponseEntity.ok("Password reset successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User newUser = userService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
