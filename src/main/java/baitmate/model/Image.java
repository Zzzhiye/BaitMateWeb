package baitmate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob private Long image;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  public Image() {}

  public Image(Long imageOid, Post post) {
    this.image = imageOid;
    this.post = post;
  }
}
