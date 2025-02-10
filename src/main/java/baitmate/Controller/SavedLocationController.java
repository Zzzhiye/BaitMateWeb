package baitmate.Controller;

import baitmate.Repository.UserRepository;
import baitmate.Service.FishingLocationService;
import baitmate.Service.UserService;
import baitmate.model.FishingLocation;
import baitmate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-locations")
public class SavedLocationController {
    @Autowired
    UserService userService;

    @Autowired
    FishingLocationService fishingLocationService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/remove")
    public ResponseEntity<?> removeSavedLocation(@RequestParam Long userId, @RequestParam Long locationId) {
        User user = userService.searchByUserId(userId);
        FishingLocation location = fishingLocationService.getFishingSpotById(locationId).orElse(null);
        if (user == null || location == null) {
            return ResponseEntity.badRequest().body("User or Location Not Found");
        }
        user.getSavedLocations().remove(location);
        userRepository.save(user);
        return ResponseEntity.ok("Location removed successfully.");
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveFishingLocation(@RequestParam Long userId, @RequestParam Long locationId) {
        User user = userService.searchByUserId(userId);
        FishingLocation location = fishingLocationService.getFishingSpotById(locationId).orElse(null);
        if (user == null || location == null) {
            System.out.println("not found");
            return ResponseEntity.badRequest().body("User or Location not found.");
        }
        user.getSavedLocations().add(location);
        userRepository.save(user);
        return ResponseEntity.ok("Location saved successfully.");
    }

    @GetMapping("/saved")
    public ResponseEntity<List<FishingLocation>> getSavedLocations(@RequestParam Long userId) {
        User user = userService.searchByUserId(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }
        List<FishingLocation> savedLocations = user.getSavedLocations();
        return ResponseEntity.ok(savedLocations);
    }
}
