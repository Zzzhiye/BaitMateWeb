package baitmate.DTO;

import java.time.LocalDateTime;
import lombok.Data;

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
