package baitmate.Service;

import baitmate.DTO.RegisterRequest;
import baitmate.model.User;

import java.util.Optional;

public interface UserService  {
    User validateUser(String username, String password);
    User registerUser(RegisterRequest registerRequest);
    String generateAndSendOTP(String email);
    boolean verifyOTP(String username, String otp);
    void updatePassword(String username, String newPassword);
    Optional<User> findByUsername(String username);
}
