package baitmate.Repository;

import baitmate.model.FishingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FishingSpotRepository extends JpaRepository<FishingSpot, Long> {
    // 模糊搜索：根据名称（忽略大小写）搜索钓鱼地点
    List<FishingSpot> findByNameContainingIgnoreCase(String name);
}




