package baitmate.Repository;

import baitmate.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  @Query("SELECT u FROM User u WHERE u.id = :userId")
  User searchByUserId(@Param("userId") long userId);

  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

  @Query("SELECT u FROM User u LEFT JOIN u.posts p GROUP BY u ORDER BY COUNT(p) DESC")
  List<User> findMostActiveUsers(Pageable pageable);

  @Query("SELECT COUNT(u) FROM User u WHERE u.joinDate BETWEEN :startDate AND :endDate")
  long countByJoinDateBetween(
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

  boolean existsByUsername(String username);

  
  @Query("SELECT f FROM User u JOIN u.following f WHERE u.id = :userId")
  List<User> findFollowing(@Param("userId") long userId);

  
  @Query("SELECT u FROM User u JOIN u.following f WHERE f.id = :userId")
  List<User> findFollowers(@Param("userId") long userId);
}
