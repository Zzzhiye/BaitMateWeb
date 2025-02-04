package baitmate.DTO;

import lombok.Data;

@Data
public class ImageDto {
    private Long id;
    private Long image;       // 如 image/png
    // private Long postId;       // 所属的 Post (可选)
}


