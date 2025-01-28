package baitmate.Controller;

import baitmate.model.FishingSpot;
import baitmate.Service.FishingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                .orElse(null); // 如果没有找到，返回 null
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

    // 搜索钓鱼地点（通过名称关键字）
    @GetMapping("/search")
    public List<FishingSpot> searchFishingSpots(@RequestParam String query) {
        return fishingSpotService.searchFishingSpots(query);
    }
}






