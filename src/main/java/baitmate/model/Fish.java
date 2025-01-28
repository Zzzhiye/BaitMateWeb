package baitmate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Fish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String FishName;
    private String FishLatinName;
    @Column(length = 2048)
    private String FishDescription;
    private String BaitType;

    @Lob
    private byte[] FishImage;

    @ManyToMany
    @JoinTable(
            name = "fish_in_fishing_location",
            joinColumns = @JoinColumn(name = "fish_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private List<FishingLocation> fishingLocations;
}
