package baitmate.DTO;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserDto {
  private Long id;
  private String username;
  private String phoneNumber;
  private String email;
  private int age;
  private String gender;
  private String address;
  private LocalDate joinDate;
  private String userStatus;
  private byte[] profileImage;
}
