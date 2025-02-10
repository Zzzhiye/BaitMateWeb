package baitmate.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CreatedPostDto {

    private String postTitle;
    private String postContent;
    private Long userId;
    private String location;
    private List<String> imageBase64List;

}
