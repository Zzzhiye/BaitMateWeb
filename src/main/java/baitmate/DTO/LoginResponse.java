package baitmate.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
  private long userId;
  private String token;
  private String errorMessage;

  public LoginResponse() {}

  public LoginResponse(long id, String token) {
    this.userId = id;
    this.token = token;
  }

  public LoginResponse(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
