package baitmate.ImplementationMethod;

import baitmate.Repository.AdminRepository;
import baitmate.Service.AdminService;
import baitmate.model.Admin;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

  private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

  @Autowired private AdminRepository adminRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public Admin searchUserByUserName(String username) {
    // TODO Auto-generated method stub
    Admin a = adminRepository.searchUserByUserName(username);
    return a;
  }

  @Override
  public void updateAdmin(Admin admin) {
    logger.info("Updating/Creating admin: {}", admin.getUsername());

    // Encode password before saving (only if it's a new password or being changed)
    if (admin.getId() == 0
        || !admin.getPassword().startsWith("$2a$")) { // Simple check to determine new password
      admin.setPassword(passwordEncoder.encode(admin.getPassword()));
    }
    adminRepository.save(admin);
  }

  @Override
  public Admin getAdminById(Integer id) {
    return adminRepository.findById(id).orElse(null);
  }

  @Override
  public Admin save(Admin admin) {
    logger.info("Saving admin: {}", admin.getUsername());
    // Always encode the password when saving (even for updates, in case it was changed)
    admin.setPassword(passwordEncoder.encode(admin.getPassword()));
    return adminRepository.save(admin);
  }

  @Override
  @Transactional
  public void updatePassword(String email, String newPassword) {
    // find admin by email
    Optional<Admin> optionalAdmin = adminRepository.findAdminByEmail(email);
    // update password
    System.out.println("Saving password");
    optionalAdmin.ifPresent(
        admin -> {
          // admin.setPassword(passwordEncoder.encode(newPassword));
          admin.setPassword(newPassword);
          adminRepository.save(admin);
        });
  }
}
