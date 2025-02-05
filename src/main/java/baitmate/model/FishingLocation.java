package baitmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    @Column
    private String address;

    @Column
    private String openingHours;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @OneToMany(mappedBy = "fishingLocation")
    private List<CatchRecord> catchRecords;

    @ManyToMany(mappedBy = "savedLocations")
    @JsonBackReference
    private List<User> usersSaved;

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