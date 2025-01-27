package baitmate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "catch_record", schema = "baitmate")
public class CatchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catchid")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fishid", nullable = false)
    private Fish fish;

    @ManyToOne
    @JoinColumn(name = "locationid", nullable = false)
    private FishingLocation location;

    @Column(name = "date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    private String image;

    private Double length;

    private Double weight;

    private String remarks;

}