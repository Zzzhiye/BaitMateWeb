package baitmate.Service;

import baitmate.DTO.PostDto;

import java.util.List;

public interface PostService {

    List<PostDto> getAllPosts();
    PostDto createPost(PostDto postDto);
    PostDto updatePost(Long postId, PostDto postDto);
    void deletePost(Long postId, Long userId);
    PostDto getPostById(Long id);
    PostDto toggleLikePost(Long postId, Long userId);
    PostDto toggleSavePost(Long postId, Long userId);
    byte[] getImageDataByOid(Long oid);
}
