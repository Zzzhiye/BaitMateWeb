package baitmate.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentDto {
  private Long id;
  private String comment;
  private LocalDateTime time;
  private int likeCount;

  // 评论对应的用户
  private UserDto user;

  // 如果前端需要，也可以存 postId 或完整 PostDto
  private Long postId;
}
