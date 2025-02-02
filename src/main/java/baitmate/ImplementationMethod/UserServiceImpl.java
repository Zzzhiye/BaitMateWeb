package baitmate.ImplementationMethod;

import baitmate.DTO.RegisterRequest;
import baitmate.Repository.UserRepository;
import baitmate.Service.UserService;
import baitmate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
        @Autowired
        UserRepository userRepo;

        public User validateUser(String username, String password) {
            Optional<User> userOptional = userRepo.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(password)) {
                    return user;
                }
            }
            throw new IllegalArgumentException("Invalid username or password");
        }

        public User registerUser(RegisterRequest registerRequest) {
            Optional<User> existingUser = userRepo.findByUsername(registerRequest.getUserName());
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("Username is already taken");
            }

            User user = new User();
            user.setUsername(registerRequest.getUserName());
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
}
