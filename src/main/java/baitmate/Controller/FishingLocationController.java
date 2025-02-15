package baitmate.Controller;

import baitmate.Service.FishingLocationService;
import baitmate.model.FishingLocation;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
public class FishingLocationController {
  @Autowired FishingLocationService fishingLocationInt;

  @GetMapping
  public List<FishingLocation> getLocations() {
    return fishingLocationInt.findAllLocations();
  }

  @GetMapping("/{id}")
  public FishingLocation getFishingSpotById(@PathVariable Long id) {
    return fishingLocationInt.getFishingSpotById(id).orElse(null);
  }

  @GetMapping("/search")
  public List<FishingLocation> searchFishingSpots(@RequestParam(required = false) String query) {
    if (query == null || query.trim().isEmpty()) {
      return Collections.emptyList(); 
    }
    return fishingLocationInt.searchFishingSpots(query);
  }

  @GetMapping("/nearby")
  public List<FishingLocation> getNearbyFishingSpots(
      @RequestParam double latitude, @RequestParam double longitude) {
    return fishingLocationInt.findNearbyFishingSpots(latitude, longitude);
  }

  @GetMapping("/suggestions")
  public List<String> getSearchSuggestions(@RequestParam String query) {
    return fishingLocationInt.getSearchSuggestions(query);
  }

  @PostMapping
  public ResponseEntity<?> saveFishingSpot(@RequestBody FishingLocation fishingSpot) {
    try {
      fishingLocationInt.saveFishingSpot(fishingSpot);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteFishingSpotById(@PathVariable Long id) {
    try {
      fishingLocationInt.deleteFishingSpotById(id);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
