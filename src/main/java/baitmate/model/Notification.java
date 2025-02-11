package baitmate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Title cannot be blank")
  private String title;

  @NotBlank(message = "Content cannot be blank")
  @Column(length = 4000)
  private String content;

  @NotNull private LocalDateTime createTime;

  private boolean isRead;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @NotNull(message = "User cannot be null")
  private User user;

  @ElementCollection
  @CollectionTable(name = "notification_attachments")
  private List<String> attachmentPaths = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    if (createTime == null) {
      createTime = LocalDateTime.now();
    }
    if (attachmentPaths == null) {
      attachmentPaths = new ArrayList<>();
    }
  }
}
