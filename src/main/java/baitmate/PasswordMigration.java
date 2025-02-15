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

  @Autowired private UserRepository userRepository; 

  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    
    List<User> users = userRepository.findAll(); 

    for (User user : users) {
      if (!isPasswordHashed(user.getPassword())) { 
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user); 
        System.out.println("Migrated password for user: " + user.getUsername());
      }
    }

    System.out.println("Password migration complete!");
  }

  
  private boolean isPasswordHashed(String password) {
    
    return password.startsWith("$2a$")
        || password.startsWith("$2b$")
        || password.startsWith("$2y$");
  }
}
