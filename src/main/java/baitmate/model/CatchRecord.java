package baitmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Lob @Column(columnDefinition = "OID")
    private byte[] image;
    private double length;
    private double weight;
    private double latitude;
    private double longitude;
    private String remark;

    @ManyToOne
    @JoinColumn(name = "fish_id")
    private Fish fish;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private FishingLocation fishingLocation;
}
