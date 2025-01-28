package baitmate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String postTitle;
    private String postContent;
    private String postStatus;
    private String location;
    private LocalDateTime postTime;
    private int likeCount;
    private int savedCount;
    private double accuracyScore;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<Image> images;

    @ManyToMany(mappedBy = "savedPosts")
    private List<User> savedByUsers;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
}

