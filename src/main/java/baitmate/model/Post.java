package baitmate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "post", schema = "baitmate")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postid")
    private Long id;

    @Column(name = "title")
    private String postTitle;

    @Column(name = "content")
    private String postContent;

    @Column(name = "poststatus")
    private String postStatus;

    @Column(name = "location")
    private String location;

    @Column(name = "posttime")
    private LocalDateTime postTime;

    @Column(name = "likecount")
    private int likeCount = 0;

    @Column(name = "savedcount")
    private int savedCount = 0;

    @Column(name = "accuracyscore")
    private double accuracyScore = 0.0;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<Image> images;

    @ManyToMany(mappedBy = "savedPosts")
    private List<User> savedByUsers;

    @ManyToMany(mappedBy = "likedPosts")
    private List<User> likedByUsers;
}

