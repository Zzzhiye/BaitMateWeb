package baitmate.Controller;

import baitmate.Service.AdminService;
import baitmate.model.Admin;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class ProfileController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Admin admin = adminService.searchUserByUserName(username);
        if (admin == null) {
            return "redirect:/login";
        }

        model.addAttribute("admin", admin);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @Valid @ModelAttribute("admin") Admin admin,
            BindingResult bindingResult,
            @RequestParam String currentPassword,
            @RequestParam(required = false) String newPassword,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model) {

        // Handle validation errors
        if (bindingResult.hasErrors()) {
            return "profile";
        }

        // Get current admin
        String username = (String) session.getAttribute("username");
        Admin currentAdmin = adminService.searchUserByUserName(username);

        if (currentAdmin == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Session expired");
            return "redirect:/login";
        }

        // Verify current password
        if (!currentAdmin.getPassword().equals(currentPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Current password is incorrect");
            return "redirect:/admin/profile";
        }

        try {
            // Set the ID
            admin.setId(currentAdmin.getId());

            // Handle password
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                // Validate new password pattern
                if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-+={[}]|:;\"'<,>.?/]).{6,10}$")) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Password must be 6-10 characters and include at least one number, uppercase letter, lowercase letter, and special character");
                    return "redirect:/admin/profile";
                }
                admin.setPassword(newPassword);
            } else {
                admin.setPassword(currentPassword);
            }

            // Update the admin
            adminService.updateAdmin(admin);

            // Update session if username changed
            if (!username.equals(admin.getUsername())) {
                session.setAttribute("username", admin.getUsername());
            }

            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/profile";
        }

        return "redirect:/admin/profile";
    }
}
