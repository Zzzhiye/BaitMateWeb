package baitmate.Repository;

import baitmate.model.FishingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FishingSpotRepository extends JpaRepository<FishingSpot, Long> {

    // **模糊搜索：根据名称（忽略大小写）搜索钓鱼地点**
    List<FishingSpot> findByNameContainingIgnoreCase(String name);

    // **搜索建议：最多返回 5 个匹配的钓鱼地点**
    List<FishingSpot> findTop5ByNameContainingIgnoreCase(String name);

    // **安全搜索：防止空查询引发异常**
    default List<FishingSpot> safeSearchByName(String query) {
        return (query == null || query.trim().isEmpty()) ? List.of() : findByNameContainingIgnoreCase(query);
    }

    // **查找附近 `radius` 公里内的钓鱼地点（基于 Haversine 公式计算距离）**
    @Query(value = "SELECT * FROM fishing_spots " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) " +
            "* cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) " +
            "* sin(radians(latitude)))) < :radius", nativeQuery = true)
    List<FishingSpot> findNearbyFishingSpots(@Param("latitude") double latitude,
                                             @Param("longitude") double longitude,
                                             @Param("radius") double radius);
}








