package baitmate.Controller;

import baitmate.model.FishingSpot;
import baitmate.Service.FishingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/fishing-spots")
public class FishingSpotController {

    @Autowired
    private FishingSpotService fishingSpotService;

    // 获取所有钓鱼地点
    @GetMapping
    public List<FishingSpot> getAllFishingSpots() {
        return fishingSpotService.getAllFishingSpots();
    }

    // 根据 ID 获取钓鱼地点
    @GetMapping("/{id}")
    public FishingSpot getFishingSpotById(@PathVariable Long id) {
        return fishingSpotService.getFishingSpotById(id)
                .orElse(null);
    }

    // 保存或更新钓鱼地点
    @PostMapping
    public FishingSpot saveFishingSpot(@RequestBody FishingSpot fishingSpot) {
        return fishingSpotService.saveFishingSpot(fishingSpot);
    }

    // 删除钓鱼地点
    @DeleteMapping("/{id}")
    public void deleteFishingSpotById(@PathVariable Long id) {
        fishingSpotService.deleteFishingSpotById(id);
    }

    // 搜索钓鱼地点（自动补全）
    @GetMapping("/search")
    public List<FishingSpot> searchFishingSpots(@RequestParam(required = false) String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList(); // 返回空列表，防止空查询导致异常
        }
        return fishingSpotService.searchFishingSpots(query);
    }


    // 获取附近 5 公里内的钓鱼地点
    @GetMapping("/nearby")
    public List<FishingSpot> getNearbyFishingSpots(@RequestParam double latitude,
                                                   @RequestParam double longitude) {
        return fishingSpotService.findNearbyFishingSpots(latitude, longitude);
    }
    // 获取搜索建议
    @GetMapping("/suggestions")
    public List<String> getSearchSuggestions(@RequestParam String query) {
        return fishingSpotService.getSearchSuggestions(query);
    }

}







