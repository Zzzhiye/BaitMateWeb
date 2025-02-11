package baitmate.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
  private String email;
  private String otp;
  private String newPassword;

  public ResetPasswordRequest() {}

  public ResetPasswordRequest(String email, String otp, String newPassword) {
    this.email = email;
    this.otp = otp;
    this.newPassword = newPassword;
  }
}
