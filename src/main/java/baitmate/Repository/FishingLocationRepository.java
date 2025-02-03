package baitmate.Repository;

import baitmate.model.FishingLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import baitmate.model.FishingLocation;
import java.util.List;

@Repository
public interface FishingLocationRepository extends JpaRepository<FishingLocation, Long> {

    @Query("SELECT l FROM FishingLocation l LEFT JOIN CatchRecord c ON c.fishingLocation = l " +
           "GROUP BY l ORDER BY COUNT(c) DESC")
    List<FishingLocation> findPopularLocations(Pageable pageable);
}
