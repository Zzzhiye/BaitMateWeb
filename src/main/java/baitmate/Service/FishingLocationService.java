package baitmate.Service;

import baitmate.model.FishingLocation;
import java.util.List;
import java.util.Optional;

public interface FishingLocationService {
  List<FishingLocation> findAllLocations();

  Optional<FishingLocation> getFishingSpotById(Long id);

  FishingLocation saveFishingSpot(FishingLocation fishingLocation);

  void deleteFishingSpotById(Long id);

  List<FishingLocation> searchFishingSpots(String query);

  List<FishingLocation> findNearbyFishingSpots(double latitude, double longitude);

  List<String> getSearchSuggestions(String query);
}
