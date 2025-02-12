package baitmate.DTO;

import lombok.Data;

@Data
public class ProfileCatchDTO {
    private String time;
    private double length;
    private double weight;
    private String locationName;
    private String fishName;
    private Long id;
}
