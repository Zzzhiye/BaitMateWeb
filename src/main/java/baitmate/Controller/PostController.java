package baitmate.Controller;

import baitmate.DTO.CreateCommentDto;
import baitmate.DTO.CreatedPostDto;
import baitmate.DTO.PostDto;
import baitmate.Service.PostService;
import baitmate.converter.PostConverter;
import baitmate.model.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostConverter postConverter;
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
    @GetMapping("/{Id}/by-user")
    public ResponseEntity<PostDto> getPostById(
            @PathVariable Long postId,
            @RequestParam(required = false) Long userId) {

        Post post = postService.findById(postId);

        PostDto dto = postConverter.toDto(post, userId);  // 这里会判断 likedByCurrentUser

        return ResponseEntity.ok(dto);
    }


    @PostMapping("/create")
    public ResponseEntity<Long> createPost(@RequestBody CreatedPostDto postDto) {
        Long postId = postService.createPost(postDto);
        return ResponseEntity.ok(postId);
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

    @GetMapping("/getPostsByDateRange")
    public ResponseEntity<?> getPostsByDateRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        try {
            List<Post> posts = postService.findByPostTimeBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
            return ResponseEntity.ok(posts);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<?> getAllPosts(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String[] sort) {  // Changed this line

        try {
            // Simple case: no filters provided
            if (status == null && location == null &&
                    page == 0 && size == 10 && (sort == null || sort.length == 0)) {  // Changed this condition
                List<PostDto> allPosts = postService.getAllPosts();
                return ResponseEntity.ok(allPosts);
            }

            // Create Sort object based on parameters
            List<Sort.Order> orders = new ArrayList<>();
            if (sort != null && sort.length > 0) {  // Added null check
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    String property = _sort[0];
                    Sort.Direction direction = _sort.length > 1 && _sort[1].equalsIgnoreCase("desc") ?
                            Sort.Direction.DESC : Sort.Direction.ASC;
                    orders.add(new Sort.Order(direction, property));
                }
            } else {
                // Default sort by postTime desc if no sort provided
                orders.add(new Sort.Order(Sort.Direction.DESC, "postTime"));
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

            // Get the paginated results
            Page<Post> postPage = postService.searchPostByFilter(
                    status != null && !status.isEmpty() ? status : null,
                    pageable
            );

            // Create response with metadata
            Map<String, Object> response = new HashMap<>();
            response.put("posts", postPage.getContent().stream()
                    .map(post -> postConverter.toDto(post))
                    .collect(Collectors.toList()));
            response.put("currentPage", postPage.getNumber());
            response.put("totalItems", postPage.getTotalElements());
            response.put("totalPages", postPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Add logging
            e.printStackTrace();  // This will help debug the issue
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving posts: " + e.getMessage());
        }
    }
    @PostMapping("/comment")
    public ResponseEntity<Long> createComment(@RequestBody CreateCommentDto commentDto) {
        Long commentId = postService.createComment(commentDto);
        return ResponseEntity.ok(commentId);
    }

    @GetMapping("/getPostsByUsername")
    public ResponseEntity<List<PostDto>> getPostsByUsername(@RequestParam String username) {
        try {
            List<PostDto> posts = postService.findByUsername(username);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}


