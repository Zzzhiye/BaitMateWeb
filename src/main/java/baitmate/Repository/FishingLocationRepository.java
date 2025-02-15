package baitmate.Repository;

import baitmate.model.FishingLocation;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FishingLocationRepository extends JpaRepository<FishingLocation, Long> {

  @Query(
      "SELECT l FROM FishingLocation l LEFT JOIN CatchRecord c ON c.fishingLocation = l "
          + "GROUP BY l ORDER BY COUNT(c) DESC")
  List<FishingLocation> findPopularLocations(Pageable pageable);

  List<FishingLocation> findByLocationNameContainingIgnoreCase(String name);

  List<FishingLocation> findTop5ByLocationNameContainingIgnoreCase(String name);

  default List<FishingLocation> safeSearchByLocationName(String query) {
    return (query == null || query.trim().isEmpty())
        ? List.of()
        : findByLocationNameContainingIgnoreCase(query);
  }

  @Query(
      value =
          "SELECT * FROM fishing_location "
              + "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) "
              + "* cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) "
              + "* sin(radians(latitude)))) < :radius",
      nativeQuery = true)
  List<FishingLocation> findNearbyFishingSpots(
      @Param("latitude") double latitude,
      @Param("longitude") double longitude,
      @Param("radius") double radius);

  @Query(value = """
    SELECT fl.location_id, fl.location_name, COUNT(cr.id) as mostCatchCount
    FROM fishing_location fl
    LEFT JOIN catch_record cr ON fl.location_id = cr.location_id
    GROUP BY fl.location_id, fl.location_name
    ORDER BY mostCatchCount DESC
    LIMIT 10
    """, nativeQuery = true)
  List<Object[]> findTop10LocationsByTotalCatches();
}
