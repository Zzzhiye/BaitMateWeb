package baitmate.Controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import baitmate.Service.AdminService;
import baitmate.model.Admin;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

  @Mock private AdminService adminService;

  @Mock private HttpSession session;

  @Mock private Model model;

  @Mock private RedirectAttributes redirectAttributes;

  @Mock private BindingResult bindingResult;

  @Mock private SessionStatus sessionStatus; // ✅ 修复 logout 的 NullPointerException

  @InjectMocks private AdminController adminController;

  private Admin mockAdmin;

  @BeforeEach
  void setUp() {
    mockAdmin = new Admin();
    mockAdmin.setId(1);
    mockAdmin.setUsername("adminUser");
    mockAdmin.setPassword("Admin123!");
    mockAdmin.setEmail("admin@example.com");
  }

  @Test
  void login_Success() {
    when(adminService.searchUserByUserName("adminUser")).thenReturn(mockAdmin);

    String view = adminController.login(mockAdmin, session, model);

    assertEquals("redirect:/admin/home", view);
    verify(session).setAttribute("username", "adminUser");
  }

  @Test
  void login_Fail_UserNotFound() {
    when(adminService.searchUserByUserName(anyString())).thenReturn(null);

    String view = adminController.login(mockAdmin, session, model);

    assertEquals("login", view);
    verify(model).addAttribute(eq("errorMsg"), anyString());
  }

  @Test
  void logout_Success() {
    String view = adminController.logout(session, model, sessionStatus);

    assertEquals("redirect:/login", view);
    verify(session).invalidate();
    verify(sessionStatus).setComplete();
  }

  @Test
  void registerAdmin_Success() {
    when(adminService.searchUserByUserName("adminUser")).thenReturn(null);
    when(bindingResult.hasErrors()).thenReturn(false);

    String view =
        adminController.registerAdmin(
            mockAdmin, bindingResult, "Admin123!", redirectAttributes, model);

    assertEquals("redirect:/login", view);
    verify(adminService).createAdmin(mockAdmin);
  }

  @Test
  void registerAdmin_Fail_UserExists() {
    when(adminService.searchUserByUserName("adminUser")).thenReturn(mockAdmin);

    String view =
        adminController.registerAdmin(
            mockAdmin, bindingResult, "Admin123!", redirectAttributes, model);

    assertEquals("register", view);
    verify(model).addAttribute(eq("errorMessage"), anyString());
  }

  @Test
  void registerAdmin_Fail_PasswordMismatch() {
    String view =
        adminController.registerAdmin(
            mockAdmin, bindingResult, "WrongPass123", redirectAttributes, model);

    assertEquals("register", view);
    verify(model).addAttribute(eq("errorMessage"), anyString());
  }
}
