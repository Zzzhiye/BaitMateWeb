package baitmate.ImplementationMethod;

import baitmate.DTO.RegisterRequest;
import baitmate.Repository.UserRepository;
import baitmate.Service.EmailService;
import baitmate.Service.UserService;
import baitmate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;


@Service
public class UserServiceImpl implements UserService {
        @Autowired
        UserRepository userRepo;

        @Autowired
        EmailService emailService;

        private Map<String, String> otpStorage = new HashMap<>();

        public Optional<User> findByUsername(String username) {
            return userRepo.findByUsername(username);
        }

        public User validateUser(String username, String password) {
            Optional<User> userOptional = userRepo.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(password)) {
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

        public User registerUser(RegisterRequest registerRequest) {
            Optional<User> existingUser = userRepo.findByUsername(registerRequest.getUsername());
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("Username is already taken");
            }

            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(registerRequest.getPassword());
            user.setPhoneNumber(registerRequest.getPhoneNumber());
            user.setEmail(registerRequest.getEmail());
            user.setAge(registerRequest.getAge());
            user.setGender(registerRequest.getGender());
            user.setAddress(registerRequest.getAddress());
            user.setJoinDate(LocalDate.now());
            user.setUserStatus("ACTIVE");

            return userRepo.save(user);
        }

    public String generateAndSendOTP(String email) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Email not registered");
        }

        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // Generate 6-digit OTP
        otpStorage.put(email, otp);

        // Send OTP via email
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
            user.setPassword(newPassword);
            userRepo.save(user);
        }
    }

    public boolean existsByUsername(String username) {
            return userRepo.existsByUsername(username);
    }

	@Override
	public User searchByUserId(long userId) {
		// TODO Auto-generated method stub
		User u= userRepo.searchByUserId(userId);
		return u ;
	}

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		User u=userRepo.save(user);
		return u;
	}
}
