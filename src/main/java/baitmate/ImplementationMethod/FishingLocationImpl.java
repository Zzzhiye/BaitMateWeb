package baitmate.ImplementationMethod;

import baitmate.Repository.FishingLocationRepository;
import baitmate.Service.FishingLocationService;
import baitmate.model.FishingLocation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FishingLocationImpl implements FishingLocationService {
    @Autowired
    FishingLocationRepository fishinglocationRepo;

    public List<FishingLocation> findAllLocations() {
        return fishinglocationRepo.findAll();
    }

    // **根据 ID 获取钓鱼地点**
    public Optional<FishingLocation> getFishingSpotById(Long id) {
        return fishinglocationRepo.findById(id);
    }

    // **保存或更新钓鱼地点**
    public FishingLocation saveFishingSpot(FishingLocation fishingLocation) {
        return fishinglocationRepo.save(fishingLocation);
    }

    // **删除钓鱼地点**
    public void deleteFishingSpotById(Long id) {
        fishinglocationRepo.deleteById(id);
    }

    // **搜索钓鱼地点**
    public List<FishingLocation> searchFishingSpots(String query) {
        return fishinglocationRepo.safeSearchByLocationName(query);
    }

    // **查找5公里范围内的钓鱼地点（确保参数匹配）**
    public List<FishingLocation> findNearbyFishingSpots(double latitude, double longitude) {
        double radius = 5.0; // 默认搜索半径为 5 公里
        return fishinglocationRepo.findNearbyFishingSpots(latitude, longitude,radius);
    }
    public List<String> getSearchSuggestions(String query) {
        return fishinglocationRepo.findTop5ByLocationNameContainingIgnoreCase(query)
                .stream()
                .map(FishingLocation::getLocationName) // 只返回地点名称
                .collect(Collectors.toList());
    }
}
