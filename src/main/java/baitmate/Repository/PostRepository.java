package baitmate.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import baitmate.model.Post;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.user ORDER BY p.postTime DESC")
    List<Post> findRecentPostsWithUser();
    
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.user ORDER BY p.likeCount DESC")
    List<Post> findTopLikedPosts(Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Post p WHERE p.postTime BETWEEN :startDate AND :endDate")
    long countPostsInDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COALESCE(SUM(p.likeCount), 0) FROM Post p")
    long sumLikeCount();
    
    @Query("SELECT COUNT(c) FROM Post p JOIN p.comments c")
    long countComments();
    
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.user WHERE p.postStatus = :status ORDER BY p.postTime DESC")
    Page<Post> searchPostByFilter(@Param("status") String status, Pageable pageable);
    
    List<Post> findByPostTimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT p FROM Post p ORDER BY p.postTime DESC")
    List<Post> findRecentPosts(Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY p.likeCount DESC")
    List<Post> findTopLikedPostsByPage(Pageable pageable);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.postTime BETWEEN :start AND :end")
    long countByPostTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT SUM(p.likeCount) FROM Post p")
    long sumLikeCountAlternative();

    @Query("SELECT COUNT(c) FROM Post p JOIN p.comments c")
    long countCommentsAlternative();

    @Query(value = "SELECT EXTRACT(HOUR FROM post_time) as hour, COUNT(*) as count " +
           "FROM post " +
           "WHERE DATE(post_time) = DATE(NOW()) " +
           "GROUP BY EXTRACT(HOUR FROM post_time) " +
           "ORDER BY hour", nativeQuery = true)
    List<Object[]> findHourlyPostCountForToday();

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.images " +
            "WHERE (:status IS NULL OR p.postStatus = :status) " +
            "AND (:location IS NULL OR p.location LIKE %:location%) " +
            "AND (:startDate IS NULL OR p.postTime >= :startDate) " +
            "AND (:endDate IS NULL OR p.postTime <= :endDate) " +
            "ORDER BY p.postTime DESC")
    Page<Post> findAllWithFilters(
            @Param("status") String status,
            @Param("location") String location,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.images " +
            "ORDER BY p.postTime DESC")
    List<Post> findAllWithDetails();

    @Query("SELECT p FROM Post p WHERE p.id IN :postIds")
    List<Post> findAllByPostIds(@Param("postIds") List<Long> postIds);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.user u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))")
    List<Post> findByUserUsername(@Param("username") String username);

}
