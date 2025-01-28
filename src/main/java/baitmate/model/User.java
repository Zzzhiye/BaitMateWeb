package baitmate.model;

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
    private Long id;

    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private int age;
    private String gender;
    private String address;
    @Lob
    private byte[] profileImage;
    private LocalDate joinDate;
    private String userStatus;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<CatchRecord> catchRecords;

    @ManyToMany
    @JoinTable(
            name = "saved_locations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private List<FishingLocation> savedLocations;

    @ManyToMany
    @JoinTable(
            name = "saved_posts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<Post> savedPosts;
}