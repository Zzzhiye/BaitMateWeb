package baitmate.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProfileCatchDTO {
    private Long id;
    private String fishName;
    private String locationName;
    private String time;
    private double length;
    private double weight;

    public ProfileCatchDTO() {}

    public ProfileCatchDTO(Long id, String fishName, String locationName, String time, double length, double weight) {
        this.id = id;
        this.fishName = fishName;
        this.locationName = locationName;
        this.time = time;
        this.length = length;
        this.weight = weight;
    }
}
