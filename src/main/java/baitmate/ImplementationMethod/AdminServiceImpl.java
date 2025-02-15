package baitmate.ImplementationMethod;

import baitmate.Repository.AdminRepository;
import baitmate.Service.AdminService;
import baitmate.model.Admin;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private AdminRepository adminRepository;


    @Override
    public Admin searchUserByUserName(String username) {
        return adminRepository.searchUserByUserName(username);
    }

    @Override
    public void createAdmin(Admin admin) {
        try {
            
            if (adminRepository.searchUserByUserName(admin.getUsername()) != null) {
                throw new RuntimeException("Username already exists");
            }

            
            if (adminRepository.findAdminByEmail(admin.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }

            adminRepository.save(admin);
            logger.info("Admin created successfully: {}", admin.getUsername());
        } catch (Exception e) {
            logger.error("Error creating admin: {}", e.getMessage());
            throw new RuntimeException("Failed to create admin: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public void updateAdmin(Admin admin) {
        try {
            Admin existingAdmin = adminRepository.findById(admin.getId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            
            if (!existingAdmin.getUsername().equals(admin.getUsername()) &&
                    adminRepository.searchUserByUserName(admin.getUsername()) != null) {
                throw new RuntimeException("Username already exists");
            }

            
            if (!existingAdmin.getEmail().equals(admin.getEmail()) &&
                    adminRepository.findAdminByEmail(admin.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }

            
            existingAdmin.setName(admin.getName());
            existingAdmin.setUsername(admin.getUsername());
            existingAdmin.setEmail(admin.getEmail());
            existingAdmin.setAddress(admin.getAddress());

            
            if (!existingAdmin.getPassword().equals(admin.getPassword())) {
                existingAdmin.setPassword(admin.getPassword());
            }

            
            adminRepository.saveAndFlush(existingAdmin);
            logger.info("Admin updated successfully: {}", existingAdmin.getUsername());
        } catch (Exception e) {
            logger.error("Error updating admin: {}", e.getMessage());
            throw new RuntimeException("Failed to update admin: " + e.getMessage(), e);
        }
    }

    @Override
    public Admin getAdminById(Integer id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public Admin save(Admin admin) {
        logger.info("Saving admin: {}", admin.getUsername());
        return adminRepository.save(admin);
    }

    @Override
    @Transactional
    public void updatePassword(String email, String newPassword) {
        adminRepository.findAdminByEmail(email).ifPresent(admin -> {
            admin.setPassword(newPassword);
            adminRepository.save(admin);
        });
    }
}
