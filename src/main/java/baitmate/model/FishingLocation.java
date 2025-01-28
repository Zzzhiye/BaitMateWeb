package baitmate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private List<User> usersSaved;

    @ManyToMany(mappedBy = "fishingLocations")
    private List<Fish> fishes;

}