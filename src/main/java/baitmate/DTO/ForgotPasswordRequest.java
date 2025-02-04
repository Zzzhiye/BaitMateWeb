package baitmate.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {
    private String username;
    private String email;

    public ForgotPasswordRequest() {}
    public ForgotPasswordRequest(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
