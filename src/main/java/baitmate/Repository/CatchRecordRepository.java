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
    
    @Query(value = "SELECT COUNT(c.id) FROM catch_record c WHERE c.time BETWEEN :startDate AND :endDate", nativeQuery = true)
    long countByTimeBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    @Query("SELECT COALESCE(COUNT(c) * 1.0 / COUNT(DISTINCT c.user), 0) FROM CatchRecord c")
    long calculateAverageCatchesPerUser();
    
    @Query(value = "SELECT f.fish_name as fishName, COUNT(*) as count, fl.location_name as location " +
           "FROM catch_record c " +
           "JOIN fish f ON c.fish_id = f.id " +
           "JOIN fishing_location fl ON c.location_id = fl.location_id " +
           "WHERE DATE(CAST(c.time AS TIMESTAMP)) = DATE(NOW()) " +
           "GROUP BY f.fish_name, fl.location_name " +
           "ORDER BY count DESC", nativeQuery = true)
    List<Object[]> findTodayMostCaughtFishWithLocation(@Param("today") String today);
    
    @Query(value = "SELECT c.* FROM catch_record c WHERE CAST(time AS TIMESTAMP) BETWEEN CAST(:start AS TIMESTAMP) AND CAST(:end AS TIMESTAMP)", nativeQuery = true)
    List<CatchRecord> findByTimeBetween(@Param("start") String start, @Param("end") String end);
    
    @Query(value = "SELECT EXTRACT(HOUR FROM CAST(time AS TIMESTAMP)) as hour, COUNT(*) as count " +
           "FROM catch_record " +
           "WHERE DATE(CAST(time AS TIMESTAMP)) = DATE(NOW()) " +
           "GROUP BY EXTRACT(HOUR FROM CAST(time AS TIMESTAMP)) " +
           "ORDER BY hour", nativeQuery = true)
    List<Object[]> findHourlyCatchCountForToday();
    
    @Query(value = "SELECT fl.location_name as name, COUNT(*) as count " +
           "FROM catch_record c " +
           "JOIN fishing_location fl ON c.location_id = fl.location_id " +
           "WHERE DATE(CAST(c.time AS TIMESTAMP)) = DATE(NOW()) " +
           "GROUP BY fl.location_name " +
           "ORDER BY count DESC", nativeQuery = true)
    List<Object[]> findTodayCatchesByLocation();

    @Query(value = "SELECT f.fish_name as name, COUNT(*) as count " +
           "FROM catch_record c " +
           "JOIN fish f ON c.fish_id = f.id " +
           "WHERE DATE(CAST(c.time AS TIMESTAMP)) = DATE(NOW()) " +
           "GROUP BY f.fish_name " +
           "ORDER BY count DESC", nativeQuery = true)
    List<Object[]> findTodayCatchesBySpecies();
    
    @Query(value = """
            SELECT 
                u.username as username,
                f.fish_name as fish_type,
                cr.weight,
                fl.location_name as location_name,
                cr.time as catch_time
            FROM baitmate.catch_record cr
            JOIN baitmate.app_user u ON cr.user_id = u.user_id
            JOIN baitmate.fish f ON cr.fish_id = f.id
            JOIN baitmate.fishing_location fl ON cr.location_id = fl.location_id
            WHERE EXTRACT(MONTH FROM CAST(cr.time AS timestamp)) = :month
            AND EXTRACT(YEAR FROM CAST(cr.time AS timestamp)) = :year
            ORDER BY cr.weight DESC
            """, nativeQuery = true)
    List<Object[]> findTopCatchesByMonth(@Param("month") int month, @Param("year") int year);
}
