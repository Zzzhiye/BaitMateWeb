package baitmate.DTO;

import baitmate.model.Fish;
import baitmate.model.FishingLocation;
import baitmate.model.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CatchRecordDTO {
    private LocalDateTime time;
    private byte[] image;
    private double length;
    private double weight;
    private double latitude;
    private double longitude;
    private String remark;
    private Long fishId;
    private Long userId;
    private Long locationId;

}
