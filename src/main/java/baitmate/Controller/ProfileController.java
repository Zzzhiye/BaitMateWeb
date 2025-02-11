package baitmate.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import baitmate.model.Admin;
import baitmate.Service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class ProfileController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        // Get current admin from session
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
            @RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String newPassword,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        // Get current admin from session
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        // Verify current admin
        Admin currentAdmin = adminService.searchUserByUserName(username);
        if (currentAdmin == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Admin not found");
            return "redirect:/admin/profile";
        }

        // Handle validation errors
        if (bindingResult.hasErrors()) {
            return "profile";
        }

        // If password change is requested
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            // Verify current password
            if (currentPassword == null || !currentAdmin.getPassword().equals(currentPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Current password is incorrect");
                return "redirect:/admin/profile";
            }

            // Set new password
            admin.setPassword(newPassword);
        } else {
            // Keep existing password if no new password is provided
            admin.setPassword(currentAdmin.getPassword());
        }

        // Ensure we're updating the correct admin
        admin.setId(currentAdmin.getId());

        try {
            adminService.updateAdmin(admin);
            // Update session with new username if it was changed
            if (!username.equals(admin.getUsername())) {
                session.setAttribute("username", admin.getUsername());
            }
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update profile: " + e.getMessage());
        }

        return "redirect:/admin/profile";
    }
}
