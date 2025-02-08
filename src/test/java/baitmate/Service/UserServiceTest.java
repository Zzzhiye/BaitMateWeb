package baitmate.Service;

import baitmate.DTO.RegisterRequest;
import baitmate.Repository.UserRepository;
import baitmate.ImplementationMethod.UserServiceImpl;
import baitmate.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)  // ✅ 允许未使用的 stub，防止 UnnecessaryStubbingException
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setPassword("encodedPassword");
        testUser.setEmail("test@example.com");
        testUser.setUserStatus("active");

        // ✅ 允许未使用的 stub，防止 UnnecessaryStubbingException
        Mockito.lenient().when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        Mockito.lenient().when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
    }

    /** ✅ 测试 validateUser: 用户名和密码匹配 */
    @Test
    void testValidateUser_Success() {
        User user = userService.validateUser("testUser", "password123");

        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
    }

    /** ✅ 测试 registerUser: 成功注册 */
    @Test
    void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newUser");
        request.setPassword("newPass123");
        request.setEmail("new@example.com");

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(passwordEncoder.encode("newPass123")).thenReturn("encodedPass");

        // ✅ 只 mock 必要的 save
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2L); // 模拟数据库存储后的 ID
            return user;
        });

        User user = userService.registerUser(request);

        assertNotNull(user);
        assertEquals("newUser", user.getUsername()); // 确保返回的用户信息正确
    }
}
