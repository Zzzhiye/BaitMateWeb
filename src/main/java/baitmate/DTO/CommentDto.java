package baitmate.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentDto {
  private Long id;
  private String comment;
  private LocalDateTime time;
  private int likeCount;
  private UserDto user;
  private Long postId;

  private boolean likedByCurrentUser;
}
