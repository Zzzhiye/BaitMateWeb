package baitmate.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(name = "username", unique = true)
  @NotBlank(message = "Username cannot be blank")
  private String username;

  @Column(name = "password")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$",
      message = "Password must contain at least one letter and one number")
  @NotBlank(message = "Password cannot be blank")
  @JsonIgnore
  private String password;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "email")
  @NotBlank(message = "Email is mandatory")
  @Email(message = "Please enter a valid email")
  private String email;

  @Column(name = "age")
  private int age;

  @Column(name = "gender")
  private String gender;

  @Column(name = "address")
  private String address;

  @Column(name = "join_date")
  private LocalDate joinDate;

  @Column(name = "user_status")
  private String userStatus;

  @OneToMany(mappedBy = "user")
  @JsonIgnore
  private List<Post> posts;

  @OneToMany(mappedBy = "user")
  @JsonIgnore
  private List<Comment> comments;

  @OneToMany(mappedBy = "user")
  @JsonIgnore
  private List<CatchRecord> catchRecords;

  @ManyToMany
  @JoinTable(
      name = "saved_locations",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "location_id"))
  @JsonBackReference
  private List<FishingLocation> savedLocations;

  @ManyToMany
  @JoinTable(
      name = "saved_posts",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "post_id"))
  @JsonBackReference
  private List<Post> savedPosts;

  @ManyToMany
  @JoinTable(
      name = "liked_posts",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "post_id"))
  @JsonBackReference
  private List<Post> likedPosts;

  @ManyToMany
  @JoinTable(
      name = "user_following",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "following_id"))
  @JsonBackReference
  private Set<User> following = new HashSet<>();

  @ManyToMany
  @JoinTable(
          name = "liked_comments",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "comment_id"))
  @JsonManagedReference
  private List<Comment> likedComments;
}
