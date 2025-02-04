package baitmate.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {
    private String username;

    public ForgotPasswordRequest() {}
    public ForgotPasswordRequest(String username) {
        this.username = username;
    }
}
