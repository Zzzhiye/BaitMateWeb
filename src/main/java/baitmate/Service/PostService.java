package baitmate.Service;

import baitmate.DTO.PostDto;
import baitmate.model.Post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    List<PostDto> getAllPosts();
    PostDto createPost(PostDto postDto);
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
}
