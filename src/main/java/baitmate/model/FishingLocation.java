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
    @Column(name = "locationid")
    private Long id;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    private String address;

    @Column(name = "latin_name")
    private String latinName;

    @Column(name = "opening_hours")
    private String openingHours;

    @ManyToMany(mappedBy = "savedLocations")
    private List<User> usersSaved;

    @OneToMany(mappedBy = "location")
    private List<CatchRecord> catchRecords;

    @ManyToMany
    @JoinTable(
            name = "fish_in_fishing_location",
            joinColumns = @JoinColumn(name = "locationid"),
            inverseJoinColumns = @JoinColumn(name = "fishid")
    )
    private List<Fish> fishes;

}