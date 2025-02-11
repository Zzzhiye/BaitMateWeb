package baitmate.Repository;

import baitmate.model.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {

  @Query("SELECT i FROM Image i where i.post.Id = :postId")
  List<Image> findByPostId(@Param("postId") Long postId);
}
