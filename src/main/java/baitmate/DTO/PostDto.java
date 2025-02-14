package baitmate.DTO;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PostDto {
  private Long id;
  private String postTitle;
  private String postContent;
  private String postStatus;
  private LocalDateTime postTime;
  private int likeCount;
  private int savedCount;
  private double accuracyScore;

  private UserDto user;

  private String location;

  private List<CommentDto> comments;
  private List<ImageDto> images;

  private boolean likedByCurrentUser;
  private boolean savedByCurrentUser;
}
