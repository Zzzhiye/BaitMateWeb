package baitmate.model;

import jakarta.persistence.*;

@Entity
public class Fish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String FishName;
    private String FishLatinName;
    private String FishDescription;
    private String BaitType;

    @Lob
    private byte[] FishImage;

    public String getFishName() {
        return FishName;
    }

    public void setFishName(String fishName) {
        FishName = fishName;
    }

    public String getFishLatinName() {
        return FishLatinName;
    }

    public void setFishLatinName(String fishLatinName) {
        FishLatinName = fishLatinName;
    }

    public String getFishDescription() {
        return FishDescription;
    }

    public void setFishDescription(String fishDescription) {
        FishDescription = fishDescription;
    }

    public String getBaitType() {
        return BaitType;
    }

    public void setBaitType(String baitType) {
        BaitType = baitType;
    }

    public byte[] getFishImage() {
        return FishImage;
    }

    public void setFishImage(byte[] fishImage) {
        FishImage = fishImage;
    }
}
