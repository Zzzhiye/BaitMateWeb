package baitmate.Repository;

import baitmate.model.FishingLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FishingLocationRepository extends JpaRepository<FishingLocation, Long> {

    @Query("SELECT l FROM FishingLocation l LEFT JOIN CatchRecord c ON c.fishingLocation = l " +
           "GROUP BY l ORDER BY COUNT(c) DESC")
    List<FishingLocation> findPopularLocations(Pageable pageable);

    List<FishingLocation> findByLocationNameContainingIgnoreCase(String name);

    List<FishingLocation> findTop5ByLocationNameContainingIgnoreCase(String name);

    default List<FishingLocation> safeSearchByLocationName(String query) {
        return (query == null || query.trim().isEmpty()) ? List.of() : findByLocationNameContainingIgnoreCase(query);
    }

    @Query(value = "SELECT * FROM fishing_location " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) " +
            "* cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) " +
            "* sin(radians(latitude)))) < :radius", nativeQuery = true)
    List<FishingLocation> findNearbyFishingSpots(@Param("latitude") double latitude,
                                             @Param("longitude") double longitude,
                                             @Param("radius") double radius);
}
