package baitmate.Service;

import baitmate.model.FishingSpot;
import baitmate.Repository.FishingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FishingSpotService {

    @Autowired
    private FishingSpotRepository fishingSpotRepository;

    // 获取所有钓鱼地点
    public List<FishingSpot> getAllFishingSpots() {
        return fishingSpotRepository.findAll();
    }

    // 根据 ID 获取钓鱼地点
    public Optional<FishingSpot> getFishingSpotById(Long id) {
        return fishingSpotRepository.findById(id);
    }

    // 保存或更新钓鱼地点
    public FishingSpot saveFishingSpot(FishingSpot fishingSpot) {
        return fishingSpotRepository.save(fishingSpot);
    }

    // 删除钓鱼地点
    public void deleteFishingSpotById(Long id) {
        fishingSpotRepository.deleteById(id);
    }

    // 搜索钓鱼地点（通过名称关键字）
    public List<FishingSpot> searchFishingSpots(String query) {
        return fishingSpotRepository.findByNameContainingIgnoreCase(query);
    }


}
