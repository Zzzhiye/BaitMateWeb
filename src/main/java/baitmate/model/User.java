package baitmate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users", schema = "baitmate")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private String phoneNumber;
    private String email;
    private int age;
    private String gender;
    private String address;
    private String profileImage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joinDate;

    private String userStatus;

    @ElementCollection
    private List<String> savedLocations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CatchRecord> catchRecords;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "saved_posts",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "postId")
    )
    private List<Post> savedPosts;

}