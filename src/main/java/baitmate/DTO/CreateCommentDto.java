package baitmate.DTO;

import lombok.Data;

@Data
public class CreateCommentDto {
    private String comment;
    private Long userId;
    private Long postId;
}
