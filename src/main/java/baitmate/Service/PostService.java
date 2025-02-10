package baitmate.Service;

import baitmate.DTO.CommentDto;
import baitmate.DTO.CreateCommentDto;
import baitmate.DTO.CreatedPostDto;
import baitmate.DTO.PostDto;
import baitmate.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    List<PostDto> getAllPosts();
    Long createPost(CreatedPostDto postDto);
    Long createComment(CreateCommentDto commentDto);
    PostDto updatePost(Long postId, PostDto postDto);
    void deletePost(Long postId, Long userId);
    PostDto getPostById(Long id);
    PostDto toggleLikePost(Long postId, Long userId);
    PostDto toggleSavePost(Long postId, Long userId);
    byte[] getImageDataByOid(Long oid);

    Page<Post> searchPostByFilter(String status, Pageable pageable);
    Post save(Post post);
    Page<Post> findAll(Pageable pageable);
    Post findById(Long id);
    Map<Integer, Long> getTodayPostActivity();

    Page<Post> getAllPostsWithFilters(String status, String location,
                                      LocalDateTime startDate, LocalDateTime endDate,
                                      Pageable pageable);
    List<Post> getAllPostsWithDetails();

    List<Post> getPostsByIds(List<Long> postIds);

}
