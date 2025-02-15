package baitmate.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String sender;

  @Column private String recipient;

  @Column private String subject;

  @Column(nullable = false)
  private String text;

  @Column private String announcementType;

  @Column private LocalDateTime sentTime;

  @PrePersist
  protected void onCreate() {
    sentTime = LocalDateTime.now();
  }

  @Column(columnDefinition = "TEXT")
  private String attachmentFileNames; 

  public void setAttachmentFileNames(List<String> fileNames) {
    this.attachmentFileNames = String.join(",", fileNames);
  }

  public List<String> getAttachmentFileNames() {
    if (attachmentFileNames == null || attachmentFileNames.isEmpty()) {
      return new ArrayList<>();
    }
    return Arrays.asList(attachmentFileNames.split(","));
  }
}
