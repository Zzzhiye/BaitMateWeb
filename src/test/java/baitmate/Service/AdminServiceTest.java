package baitmate.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import baitmate.ImplementationMethod.AdminServiceImpl;
import baitmate.Repository.AdminRepository;
import baitmate.model.Admin;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

  @Mock private AdminRepository adminRepository;

  @InjectMocks private AdminServiceImpl adminService;

  private Admin testAdmin;

  @Mock private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    testAdmin = new Admin();
    testAdmin.setId(1);
    testAdmin.setName("Admin User");
    testAdmin.setUsername("admin123");
    testAdmin.setEmail("admin@yahoo.com.sg");
    testAdmin.setAddress("123 Admin Street");
    testAdmin.setPassword("Secure@123");
    Mockito.lenient().when(passwordEncoder.encode(any())).thenReturn("EncodeSecure@123");
  }

  
  @Test
  void testSearchUserByUserName_Found() {

    when(adminRepository.searchUserByUserName(any())).thenReturn(testAdmin);
    Admin admin = adminService.searchUserByUserName("admin123");

    assertNotNull(admin);
    assertEquals("admin123", admin.getUsername());
  }

  
  @Test
  void testGetAdminById_Found() {
    when(adminRepository.findById(1)).thenReturn(Optional.of(testAdmin)); 

    Admin admin = adminService.getAdminById(1);

    assertNotNull(admin);
    assertEquals(1, admin.getId());
  }

  
  @Test
  void testGetAdminById_NotFound() {
    when(adminRepository.findById(2)).thenReturn(Optional.empty()); 

    Admin admin = adminService.getAdminById(2);

    assertNull(admin);
  }

  
  @Test
  void testUpdateAdmin_Success() {
    Integer adminId = testAdmin.getId();

    
    when(adminRepository.findById(adminId))
        .thenReturn(Optional.of(testAdmin));
    
    when(adminRepository.saveAndFlush(any(Admin.class))).thenReturn(testAdmin);

    adminService.updateAdmin(testAdmin);

    
    verify(adminRepository).saveAndFlush(testAdmin);
  }

  @Test
  void testSaveAdmin_Success() {
    when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);
    Admin savedAdmin = adminService.save(testAdmin);
    assertNotNull(savedAdmin);
    verify(adminRepository).save(testAdmin);
  }
}
