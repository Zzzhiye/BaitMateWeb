package baitmate.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import baitmate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.id = :userId")
    User searchByUserId(@Param("userId") long userId);
    
    Optional<User> findByUsername(String username);
    
    @Query("SELECT u FROM User u LEFT JOIN u.posts p GROUP BY u ORDER BY COUNT(p) DESC")
    List<User> findMostActiveUsers(Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.joinDate BETWEEN :startDate AND :endDate")
    long countByJoinDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
