package baitmate.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminViewController {

  @GetMapping("/AdminForgotPassword")
  public String showForgotPasswordPage() {
    return "AdminForgotPassword";
  }

  @GetMapping("/AdminResetPassword")
  public String showResetPasswordPage() {
    return "AdminResetPassword"; // Corrected: No .html extension
  }
}
