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
  // 注意: password 通常不返回
  // private String password;
  // 如果有头像可以加上profileImage的处理(如Base64之类的)
  private byte[] profileImage;
}
