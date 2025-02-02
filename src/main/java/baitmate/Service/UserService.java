package baitmate.Service;

import baitmate.DTO.RegisterRequest;
import baitmate.model.User;

public interface UserService  {
    User validateUser(String username, String password);
    User registerUser(RegisterRequest registerRequest);
}
