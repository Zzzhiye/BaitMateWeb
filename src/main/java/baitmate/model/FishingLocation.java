package baitmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "fishing_location", schema = "baitmate")
public class FishingLocation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "location_id")
  private Long id;

  @Column(name = "location_name", nullable = false)
  private String locationName;

  @Column private String address;

  @Column private String openingHours;

  @Column private double latitude;

  @Column private double longitude;

  @JsonIgnore
  @OneToMany(mappedBy = "fishingLocation")
  @JsonManagedReference
  private List<CatchRecord> catchRecords;

  @JsonIgnore
  @ManyToMany(mappedBy = "savedLocations")
  @JsonBackReference
  private List<User> usersSaved;

  @JsonIgnore
  @ManyToMany(mappedBy = "fishingLocations")
  private List<Fish> fishes;

  @Transient
  public String getStatus() {
    if (openingHours == null || openingHours.equals("24/7")) {
      return "Open";
    }

    try {
      String[] times = openingHours.split("-");
      LocalTime openTime = LocalTime.parse(times[0], DateTimeFormatter.ofPattern("HH:mm"));
      LocalTime closeTime = LocalTime.parse(times[1], DateTimeFormatter.ofPattern("HH:mm"));
      LocalTime currentTime = LocalTime.now();

      return (currentTime.isAfter(openTime) && currentTime.isBefore(closeTime)) ? "Open" : "Closed";
    } catch (Exception e) {
      return "Unknown";
    }
  }
}
