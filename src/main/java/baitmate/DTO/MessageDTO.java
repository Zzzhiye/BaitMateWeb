package baitmate.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
public class MessageDTO {
  private String sender;
  private String recipient;
  private String subject;
  private String text;
  private String announcementType;
  private List<String> attachmentFileNames;
  private MultipartFile[] attachments;

  public MessageDTO() {}
}
