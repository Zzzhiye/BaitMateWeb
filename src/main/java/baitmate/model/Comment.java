package baitmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String comment;
  private LocalDateTime time;
  private int likeCount;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private User user;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;
}
