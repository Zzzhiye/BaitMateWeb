package baitmate.DTO;

import java.util.List;
import lombok.Data;

@Data
public class CreatedPostDto {

  private String postTitle;
  private String postContent;
  private Long userId;
  private String location;
  private String status;
  private List<String> imageBase64List;
}
