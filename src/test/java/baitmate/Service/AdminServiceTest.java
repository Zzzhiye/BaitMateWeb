package baitmate.Service;

import baitmate.ImplementationMethod.AdminServiceImpl;
import baitmate.Repository.AdminRepository;
import baitmate.model.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Admin testAdmin;

    @BeforeEach
    void setUp() {
        testAdmin = new Admin();
        testAdmin.setId(1);
        testAdmin.setName("Admin User");
        testAdmin.setUsername("admin123");
        testAdmin.setEmail("admin@example.com");
        testAdmin.setAddress("123 Admin Street");
        testAdmin.setPassword("Secure@123");
    }

    /** ✅ 测试 searchUserByUserName: 找到用户 */
    @Test
    void testSearchUserByUserName_Found() {
        when(adminRepository.searchUserByUserName("admin123")).thenReturn(testAdmin);

        Admin admin = adminService.searchUserByUserName("admin123");

        assertNotNull(admin);
        assertEquals("admin123", admin.getUsername());
    }

    /** ✅ 测试 getAdminById: 找到管理员 */
    @Test
    void testGetAdminById_Found() {
        when(adminRepository.findById(1)).thenReturn(Optional.of(testAdmin));

        Admin admin = adminService.getAdminById(1);

        assertNotNull(admin);
        assertEquals(1, admin.getId());
    }

    /** ✅ 测试 getAdminById: 找不到管理员 */
    @Test
    void testGetAdminById_NotFound() {
        when(adminRepository.findById(2)).thenReturn(Optional.empty());

        Admin admin = adminService.getAdminById(2);

        assertNull(admin);
    }

    /** ✅ 测试 updateAdmin: 成功更新管理员 */
    @Test
    void testUpdateAdmin_Success() {
        when(adminRepository.save(testAdmin)).thenReturn(testAdmin);

        Admin updatedAdmin = adminService.updateAdmin(testAdmin);

        assertNotNull(updatedAdmin);
        assertEquals("Admin User", updatedAdmin.getName());
    }
}
