package baitmate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "user_status")
    private String userStatus;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<CatchRecord> catchRecords;

    @ManyToMany
    @JoinTable(
            name = "saved_locations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    @JsonManagedReference
    private List<FishingLocation> savedLocations;

    @ManyToMany
    @JoinTable(
            name = "saved_posts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    @JsonManagedReference
    private List<Post> savedPosts;

    @ManyToMany
    @JoinTable(
            name = "liked_posts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    @JsonManagedReference
    private List<Post> likedPosts;
}