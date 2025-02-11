package baitmate.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
  private String username;
  private String password;
  private String phoneNumber;
  private String email;
  private String address;
  private String gender;
  private int age;

  public RegisterRequest() {}
}
