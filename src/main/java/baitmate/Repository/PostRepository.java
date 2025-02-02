package baitmate.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
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
    long countByPostTimeBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COALESCE(SUM(p.likeCount), 0) FROM Post p")
    long sumLikeCount();
    
    @Query("SELECT COUNT(c) FROM Post p JOIN p.comments c")
    long countComments();
}
