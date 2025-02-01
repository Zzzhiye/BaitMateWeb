package baitmate.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import baitmate.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("Select p from Post p where p.postStatus =:postStatus")
	Page<Post> searchPostByFilter(@Param("postStatus") String postStatus, Pageable pageable);

	@Query("Select p from Post p where p.postStatus !=:postStatus")
	Page<Post> searchPostByPostStatus(@Param("postStatus") String postStatus, Pageable pageable);

	@Query("Select p from Post p where p.Id =:postId")
	public Post searchPostByPostId(@Param("postId") long postId);

}
