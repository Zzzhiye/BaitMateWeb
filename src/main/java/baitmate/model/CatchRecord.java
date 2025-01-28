package baitmate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CatchRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String time;
    private byte[] image;
    private double length;
    private double weight;
    private double latitude;
    private double longitude;
    private String remark;

    @ManyToOne
    @JoinColumn(name = "fish_id")
    private Fish fish;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "spot_id")
    private FishingSpot fishingSpot;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private FishingLocation fishingLocation;
}
