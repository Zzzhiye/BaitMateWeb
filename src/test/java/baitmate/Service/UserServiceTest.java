package baitmate.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import baitmate.DTO.RegisterRequest;
import baitmate.ImplementationMethod.UserServiceImpl;
import baitmate.Repository.UserRepository;
import baitmate.model.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) 
class UserServiceTest {

  @Mock private UserRepository userRepo;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserServiceImpl userService;

  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testUser");
    testUser.setPassword("encodedPassword");
    testUser.setEmail("test@example.com");
    testUser.setUserStatus("active");

    
    Mockito.lenient().when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(testUser));
    Mockito.lenient()
        .when(passwordEncoder.matches("password123", "encodedPassword"))
        .thenReturn(true);
  }

  
  @Test
  void testValidateUser_Success() {
    User user = userService.validateUser("testUser", "password123");

    assertNotNull(user);
    assertEquals("testUser", user.getUsername());
  }

  
  @Test
  void testRegisterUser_Success() {
    RegisterRequest request = new RegisterRequest();
    request.setUsername("newUser");
    request.setPassword("newPass123");
    request.setEmail("new@example.com");

    when(userRepo.existsByUsername("newUser")).thenReturn(false);
    when(passwordEncoder.encode("newPass123")).thenReturn("encodedPass");

    
    when(userRepo.save(any(User.class)))
        .thenAnswer(
            invocation -> {
              User user = invocation.getArgument(0);
              user.setId(2L); 
              return user;
            });

    User user = userService.registerUser(request);

    assertNotNull(user);
    assertEquals("newUser", user.getUsername()); 
  }
}
