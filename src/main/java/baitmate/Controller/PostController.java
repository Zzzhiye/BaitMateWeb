package baitmate.Controller;

import baitmate.DTO.PostDto;
import baitmate.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        try {
            PostDto dto = postService.getPostById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        PostDto created = postService.createPost(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 删除 post
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @RequestParam Long userId) {
        // userId 可从 token 或请求里获取
        try {
            postService.deletePost(postId, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{postId}/like")
    public ResponseEntity<PostDto> toggleLike(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        try {
            PostDto updated = postService.toggleLikePost(postId, userId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{postId}/save")
    public ResponseEntity<PostDto> toggleSave(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        try {
            PostDto updated = postService.toggleSavePost(postId, userId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}


