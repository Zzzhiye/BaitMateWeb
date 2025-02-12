package baitmate.Service;

import baitmate.DTO.PostDto;
import baitmate.DTO.RegisterRequest;
import baitmate.DTO.UserDto;
import baitmate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService  {
    User validateUser(String username, String password);
    User registerUser(RegisterRequest registerRequest);
    String generateAndSendOTP(String email);
    boolean verifyOTP(String username, String otp);
    void updatePassword(String email, String newPassword);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    User searchByUserId(long userId);
    User save(User user);
    List<User> getAllUsers();
    List<User> findFollowing(long userId);
    List<User> findFollowers(long userId);
    void followUser(long userId, long targetId);
    void unfollowUser(long userId, long targetId);
    UserDto getUserById(Long id);
}
