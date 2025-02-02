package baitmate.model;


import jakarta.validation.constraints.Pattern;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String username;
	private String email;
	private String address;
	@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\\\\\\\-\\\\\\\\[\\\\\\\\]{};':\\\\\\\"\\\\\\\\\\\\\\\\|,.<>\\\\\\\\/?]).{6,10}$", message = "Password must contain at least 6 characters at most 10 characters, including uppercase, lowercase letters, special character and numbers.")
	private String password;
}
