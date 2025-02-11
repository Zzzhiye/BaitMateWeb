package baitmate.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    public MessageDTO() {

    }
}