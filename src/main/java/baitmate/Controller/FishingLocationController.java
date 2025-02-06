package baitmate.Controller;

import baitmate.ImplementationMethod.FishingLocationImpl;
import baitmate.Service.FishingLocationService;
import baitmate.model.FishingLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class FishingLocationController {
    @Autowired
    FishingLocationService fishingLocationInt;
    @Autowired
    private FishingLocationImpl fishingLocationImpl;

    @GetMapping
    public List<FishingLocation> getLocations() {
        return fishingLocationInt.findAllLocations();
    }

    @GetMapping("/{id}")
    public FishingLocation getFishingSpotById(@PathVariable Long id) {
        return fishingLocationImpl.getFishingSpotById(id)
                .orElse(null);
    }

    @GetMapping("/search")
    public List<FishingLocation> searchFishingSpots(@RequestParam(required = false) String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList(); // 返回空列表，防止空查询导致异常
        }
        return fishingLocationImpl.searchFishingSpots(query);
    }

    @GetMapping("/nearby")
    public List<FishingLocation> getNearbyFishingSpots(@RequestParam double latitude,
                                                   @RequestParam double longitude) {
        return fishingLocationImpl.findNearbyFishingSpots(latitude, longitude);
    }

    @GetMapping("/suggestions")
    public List<String> getSearchSuggestions(@RequestParam String query) {
        return fishingLocationImpl.getSearchSuggestions(query);
    }

    @PostMapping
    public ResponseEntity<?> saveFishingSpot(@RequestBody FishingLocation fishingSpot) {
        try {
            fishingLocationImpl.saveFishingSpot(fishingSpot);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 删除钓鱼地点
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFishingSpotById(@PathVariable Long id) {
        try {
            fishingLocationImpl.deleteFishingSpotById(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
