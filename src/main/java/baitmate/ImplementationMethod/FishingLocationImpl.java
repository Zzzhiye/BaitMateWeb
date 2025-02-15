package baitmate.ImplementationMethod;

import baitmate.Repository.FishingLocationRepository;
import baitmate.Service.FishingLocationService;
import baitmate.model.FishingLocation;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FishingLocationImpl implements FishingLocationService {
  @Autowired FishingLocationRepository fishinglocationRepo;

  public List<FishingLocation> findAllLocations() {
    return fishinglocationRepo.findAll();
  }

  
  public Optional<FishingLocation> getFishingSpotById(Long id) {
    return fishinglocationRepo.findById(id);
  }

  
  public FishingLocation saveFishingSpot(FishingLocation fishingLocation) {
    return fishinglocationRepo.save(fishingLocation);
  }

  
  public void deleteFishingSpotById(Long id) {
    fishinglocationRepo.deleteById(id);
  }

  
  public List<FishingLocation> searchFishingSpots(String query) {
    return fishinglocationRepo.safeSearchByLocationName(query);
  }

  
  public List<FishingLocation> findNearbyFishingSpots(double latitude, double longitude) {
    double radius = 5.0; 
    return fishinglocationRepo.findNearbyFishingSpots(latitude, longitude, radius);
  }

  public List<String> getSearchSuggestions(String query) {
    return fishinglocationRepo.findTop5ByLocationNameContainingIgnoreCase(query).stream()
        .map(FishingLocation::getLocationName) 
        .collect(Collectors.toList());
  }
}
