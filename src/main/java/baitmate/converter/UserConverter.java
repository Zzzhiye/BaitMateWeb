package baitmate.converter;

import baitmate.DTO.UserDto;
import baitmate.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

  public UserDto toDto(User user) {
    if (user == null) return null;
    UserDto dto = new UserDto();
    dto.setId(user.getId());
    dto.setUsername(user.getUsername());
    dto.setPhoneNumber(user.getPhoneNumber());
    dto.setEmail(user.getEmail());
    dto.setAge(user.getAge());
    dto.setGender(user.getGender());
    dto.setAddress(user.getAddress());
    dto.setJoinDate(user.getJoinDate());
    dto.setUserStatus(user.getUserStatus());
    
    return dto;
  }

  public User toEntity(UserDto dto) {
    if (dto == null) return null;
    User user = new User();
    user.setId(dto.getId());
    user.setUsername(dto.getUsername());
    user.setPhoneNumber(dto.getPhoneNumber());
    user.setEmail(dto.getEmail());
    user.setAge(dto.getAge());
    user.setGender(dto.getGender());
    user.setAddress(dto.getAddress());
    user.setJoinDate(dto.getJoinDate());
    user.setUserStatus(dto.getUserStatus());
      
    return user;
  }
}
