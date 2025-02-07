package baitmate.Controller;

import baitmate.DTO.PostDto;
import baitmate.Service.PostService;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private DataSource dataSource;

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

    @GetMapping("/oid/{oid}")
    public ResponseEntity<byte[]> getImageByOid(@PathVariable Long oid) {
        try {
            // 调用 Service 获取二进制
            byte[] imageData = postService.getImageDataByOid(oid);

            HttpHeaders headers = new HttpHeaders();
            // 如果知道具体图片类型，比如 PNG，就用 IMAGE_PNG
            // 如果不确定，就用 OCTET_STREAM
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);

        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

}


