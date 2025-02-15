package baitmate.Controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import baitmate.DTO.*;
import baitmate.Service.TokenService;
import baitmate.Service.UserService;
import baitmate.model.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock private UserService userService;

  @Mock private TokenService tokenService;

  @InjectMocks private UserController userController;

  private User mockUser;

  @BeforeEach
  void setUp() {
    mockUser = new User();
    mockUser.setId(1L);
    mockUser.setUsername("testUser");
    mockUser.setPassword("Test1234");
    mockUser.setEmail("test@example.com");
  }

  
  @Test
  void login_Success() {
    LoginRequest request = new LoginRequest("testUser", "Test1234");
    when(userService.validateUser("testUser", "Test1234")).thenReturn(mockUser);
    when(tokenService.generateToken(mockUser)).thenReturn("mockToken");

    ResponseEntity<?> response = userController.login(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody() instanceof LoginResponse);
    assertEquals("mockToken", ((LoginResponse) response.getBody()).getToken());
  }

  
  @Test
  void login_Fail_InvalidUser() {
    LoginRequest request = new LoginRequest("invalidUser", "wrongPass");
    when(userService.validateUser("invalidUser", "wrongPass"))
        .thenThrow(new IllegalArgumentException("Invalid credentials"));

    ResponseEntity<?> response = userController.login(request);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  
  @Test
  void logout_Success() {
    when(tokenService.deactivateToken("validToken")).thenReturn(true);

    ResponseEntity<?> response = userController.logout("validToken");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Logged out successfully", response.getBody());
  }

  
  @Test
  void logout_Fail_InvalidToken() {
    when(tokenService.deactivateToken("invalidToken")).thenReturn(false);

    ResponseEntity<?> response = userController.logout("invalidToken");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Token not found or already invalidated", response.getBody());
  }

  
  @Test
  void forgotPassword_Success() {
    ForgotPasswordRequest request = new ForgotPasswordRequest("testUser", "test@example.com");
    when(userService.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
    when(userService.generateAndSendOTP("test@example.com")).thenReturn("123456");

    ResponseEntity<?> response = userController.forgotPassword(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("OTP sent successfully to test@example.com", response.getBody());
  }

  
  @Test
  void forgotPassword_Fail_UserNotFound() {
    ForgotPasswordRequest request = new ForgotPasswordRequest("unknownUser", "unknown@example.com");
    when(userService.findByUsername("unknownUser")).thenReturn(Optional.empty());

    ResponseEntity<?> response = userController.forgotPassword(request);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("User not found", response.getBody());
  }

  
  @Test
  void resetPassword_Success() {
    ResetPasswordRequest request =
        new ResetPasswordRequest("test@example.com", "123456", "NewPassword123");
    when(userService.verifyOTP("test@example.com", "123456")).thenReturn(true);

    ResponseEntity<?> response = userController.resetPassword(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Password reset successfully", response.getBody());
  }

  
  @Test
  void resetPassword_Fail_InvalidOTP() {
    ResetPasswordRequest request =
        new ResetPasswordRequest("test@example.com", "wrongOTP", "NewPassword123");
    when(userService.verifyOTP("test@example.com", "wrongOTP")).thenReturn(false);

    ResponseEntity<?> response = userController.resetPassword(request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid OTP", response.getBody());
  }
}
