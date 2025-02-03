package baitmate.DTO;

import baitmate.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private User user;
    private String token;
    private String errorMessage;

    public LoginResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public LoginResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
