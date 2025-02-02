package baitmate.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import baitmate.model.CatchRecord;
import java.util.List;

@Repository
public interface CatchRecordRepository extends JpaRepository<CatchRecord, Long> {
    
    @Query("SELECT c FROM CatchRecord c ORDER BY c.weight DESC")
    List<CatchRecord> findTopCatchesByWeight(Pageable pageable);
    
    @Query(value = "SELECT COUNT(c.id) FROM catch_record c WHERE TO_DATE(c.time, 'YYYY-MM-DD') BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD')", nativeQuery = true)
    long countByTimeBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    @Query("SELECT COALESCE(COUNT(c) * 1.0 / COUNT(DISTINCT c.user), 0) FROM CatchRecord c")
    long calculateAverageCatchesPerUser();
    
    @Query("SELECT c.fish.FishName as fishName, COUNT(c) as count, c.fishingLocation.locationName as location " +
           "FROM CatchRecord c " +
           "WHERE TO_DATE(c.time, 'YYYY-MM-DD') = TO_DATE(:today, 'YYYY-MM-DD') " +
           "GROUP BY c.fish.FishName, c.fishingLocation.locationName " +
           "ORDER BY count DESC")
    List<Object[]> findTodayMostCaughtFishWithLocation(@Param("today") String today);
}
