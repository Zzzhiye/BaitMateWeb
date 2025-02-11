package baitmate.Service;

import baitmate.model.Admin;
import java.util.Optional;

public interface AdminService {
    Admin searchUserByUserName(String username);

    Admin getAdminById(Integer  id);

    void updatePassword(String email, String newPassword);
    
    Admin save(Admin admin);// Standardized method name
    void updateAdmin(Admin admin);
   // Added missing method
}