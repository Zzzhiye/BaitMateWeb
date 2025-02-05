package baitmate.DTO;

import lombok.Data;

import java.time.LocalDateTime;

import java.util.List;

@Data // 或 @Getter + @Setter
public class PostDto {
    private Long id;
    private String postTitle;
    private String postContent;
    private String postStatus;
    private LocalDateTime postTime; // 返回给前端可保持 LocalDateTime 或转成 String
    private int likeCount;
    private int savedCount;
    private double accuracyScore;

    private UserDto user;

    private String location;

    // 如果前端也需要 comments、images，可再加 DTO 列表
    private List<CommentDto> comments;
    private List<ImageDto> images;

    // 如果想返回 savedByUsers 数量，而不需要每个用户信息
    private int savedByCount;

    private boolean likedByCurrentUser;
}

