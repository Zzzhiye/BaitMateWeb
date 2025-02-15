package baitmate.Service;

import baitmate.model.Admin;

public interface AdminService {
  Admin searchUserByUserName(String username);

  Admin getAdminById(Integer id);

  void updatePassword(String email, String newPassword);

  Admin save(Admin admin); // Standardized method name

    void createAdmin(Admin admin);

  void updateAdmin(Admin admin);
  // Added missing method
}
