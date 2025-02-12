package baitmate.Repository;

import baitmate.model.RedDot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedDotRepository extends JpaRepository<RedDot, Long> {
    List<RedDot> findByReceiverIdOrderByTimeDesc(Long receiverId);
}

