package baitmate.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "post", schema = "baitmate")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "title")
    private String postTitle;

    @Column(name = "content")
    private String postContent;

    @Column(name = "post_status")
    private String postStatus;

    @Column(name = "location")
    private String location;

    @Column(name = "post_time")
    private LocalDateTime postTime;

    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "saved_count")
    private int savedCount = 0;

    @Column(name = "accuracy_score")
    private double accuracyScore = 0.0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<Image> images;

    @ManyToMany(mappedBy = "savedPosts")
    @JsonBackReference
    private List<User> savedByUsers;

    @ManyToMany(mappedBy = "likedPosts")
    @JsonBackReference
    private List<User> likedByUsers;
}

