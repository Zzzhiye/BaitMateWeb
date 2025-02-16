package baitmate.ImplementationMethod;

import baitmate.DTO.PostDto;
import baitmate.DTO.RegisterRequest;
import baitmate.DTO.UserDto;
import baitmate.Repository.UserRepository;
import baitmate.Service.EmailService;
import baitmate.Service.UserService;
import baitmate.converter.UserConverter;
import baitmate.model.Post;
import baitmate.model.User;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Map<String, String> otpStorage = new HashMap<>();
    @Autowired
    UserConverter userConverter;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Transactional
    public User validateUser(String username, String password) {
        Optional<User> userOptional = userRepo.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                if ("active".equalsIgnoreCase(user.getUserStatus())) {
                    return user;
                } else {
                    throw new IllegalArgumentException("Account status is inactive.");
                }
            } else {
                throw new IllegalArgumentException("Invalid password.");
            }
        }
        throw new IllegalArgumentException("Username does not exist.");
    }

    @Transactional
    public User registerUser(RegisterRequest registerRequest) {
        Optional<User> existingUser = userRepo.findByUsername(registerRequest.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setEmail(registerRequest.getEmail());
        user.setAge(registerRequest.getAge());
        user.setGender(registerRequest.getGender());
        user.setAddress(registerRequest.getAddress());
        user.setJoinDate(LocalDate.now());
        user.setUserStatus("active");

        return userRepo.save(user);
    }

    public String generateAndSendOTP(String email) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Email not registered");
        }

        String otp = String.valueOf(new Random().nextInt(900000) + 100000); 
        otpStorage.put(email, otp);

        
        emailService.sendSimpleMessage(email, "Password Reset OTP", "Your OTP is: " + otp);

        return otp;
    }

    public boolean verifyOTP(String email, String otp) {
        return otpStorage.containsKey(email) && otpStorage.get(email).equals(otp);
    }

    public void updatePassword(String email, String newPassword) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
        }
    }

    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    @Transactional
    public User searchByUserId(long userId) {
        
        User u = userRepo.searchByUserId(userId);
        return u;
    }

    @Override

    public User save(User user) {
        
        User u = userRepo.save(user);
        return u;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public UserDto getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userConverter.toDto(user);
    }

    @Transactional
    public List<User> findFollowing(long userId) {
        return userRepo.findFollowing(userId);
    }

    @Transactional
    public List<User> findFollowers(long userId) {
        return userRepo.findFollowers(userId);
    }


    public void followUser(long userId, long targetId) {
        User user = userRepo.searchByUserId(userId);
        User targetUser = userRepo.searchByUserId(targetId);
        if (user.getFollowing().contains(targetUser)) {
            throw new IllegalArgumentException("User is already following the target user.");
        }
        user.getFollowing().add(targetUser);
        userRepo.save(user);
    }

    public void unfollowUser(long userId, long targetId) {
        User user = userRepo.searchByUserId(userId);
        User targetUser = userRepo.searchByUserId(targetId);
        if (!user.getFollowing().contains(targetUser)) {
            throw new IllegalArgumentException("User is not following the target user.");
        }
        user.getFollowing().remove(targetUser);
        userRepo.save(user);
    }

}

