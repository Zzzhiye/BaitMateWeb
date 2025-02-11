package baitmate;

import baitmate.Repository.UserRepository;
import baitmate.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordMigration implements CommandLineRunner {

  @Autowired private UserRepository userRepository; // Replace with your actual User repository

  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    // Fetch all users whose passwords are in plaintext (you can adjust this filter as needed)
    List<User> users = userRepository.findAll(); // Fetch all users

    for (User user : users) {
      if (!isPasswordHashed(user.getPassword())) { // Check if password is already hashed
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user); // Save the updated user
        System.out.println("Migrated password for user: " + user.getUsername());
      }
    }

    System.out.println("Password migration complete!");
  }

  // Helper method to check if a password is already hashed
  private boolean isPasswordHashed(String password) {
    // BCrypt hashes start with $2a, $2b, or $2y
    return password.startsWith("$2a$")
        || password.startsWith("$2b$")
        || password.startsWith("$2y$");
  }
}
