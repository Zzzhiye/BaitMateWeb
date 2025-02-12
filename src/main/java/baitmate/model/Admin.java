package baitmate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Admin {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Please provide a valid email address")
  private String email;

  private String address;

  @NotBlank(message = "Password is required")
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-+={[}]|:;\"'<,>.?/]).{6,10}$",
      message =
          "Password must be 6-10 characters and include at least one number, uppercase letter, lowercase letter, and special character")
  private String password;

  public Admin() {
    super();
  }
}
