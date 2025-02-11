package baitmate.ImplementationMethod;

import baitmate.DTO.CreateCommentDto;
import baitmate.DTO.CreatedPostDto;
import baitmate.DTO.PostDto;
import baitmate.Repository.*;
import baitmate.Service.PostService;
import baitmate.converter.PostConverter;
import baitmate.converter.UserConverter;
import baitmate.model.Comment;
import baitmate.model.Image;
import baitmate.model.Post;
import baitmate.model.User;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FishingLocationRepository fishingLocationRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostConverter postConverter;
    @Autowired
    private UserConverter userConverter;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        Collectors Collectors = null;
        return posts.stream()
                .map(postConverter::toDto)
                .collect(Collectors.toList());
    }

    public List<PostDto> getPostByUser(Long userId) {
        User user = userRepository.searchByUserId(userId);
        if (user !=null) {
            System.out.println("Retrieving posts by "+ userId);
            List<Post> posts = postRepository.findAllByUser(user);
            Collectors Collectors = null;
            return posts.stream()
                    .map(postConverter::toDto)
                    .collect(Collectors.toList());
        } else return null;
    }

    @Transactional
    public Long createPost(CreatedPostDto postDto) {
        Post post = new Post();
        post.setPostTitle(postDto.getPostTitle());
        post.setPostContent(postDto.getPostContent());
        post.setLocation(postDto.getLocation());

        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        post.setUser(user);

        post.setPostTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        post.setLikeCount(0);
        post.setSavedCount(0);
        post.setPostStatus(postDto.getStatus());

        Post saved = postRepository.save(post);

        List<Image> images = postDto.getImageBase64List().stream()
                .map(base64String -> {
                    byte[] imageBytes = Base64.getDecoder().decode(base64String);
                    Long oid = saveImageToDatabase(imageBytes);
                    return new Image(oid, post);
                })
                .collect(Collectors.toList());

        post.setImages(images);
        imageRepository.saveAll(images);

        return post.getId();
    }

    public PostDto updatePost(Long postId, PostDto postDto) {

        Post existing = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        existing.setPostTitle(postDto.getPostTitle());
        existing.setPostContent(postDto.getPostContent());


        existing.getImages().clear();

        List<Image> newImages = postConverter.toImageEntityList(postDto.getImages(), existing);
        existing.getImages().addAll(newImages);

        Post updated = postRepository.save(existing);

        return postConverter.toDto(updated);
    }

    // 2. 删除 Post
    public void deletePost(Long postId, Long userId) {
        // 只有发帖人可以删除
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not the owner of this post.");
        }
        postRepository.delete(post);
    }

    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return postConverter.toDto(post);
    }

    public PostDto toggleLikePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Post> likedPosts = user.getLikedPosts();

        if (likedPosts.contains(post)) {
            // 已点赞 -> 取消点赞
            likedPosts.remove(post);
            post.getLikedByUsers().remove(user);
            // likeCount -1
            post.setLikeCount(post.getLikeCount() - 1);
        } else {
            // 未点赞 -> 点赞
            likedPosts.add(post);
            post.getLikedByUsers().add(user);
            // likeCount +1
            post.setLikeCount(post.getLikeCount() + 1);
        }

        // 保存
        // 注意顺序，一般先保存 post 再保存 user 或都保存
        postRepository.save(post);
        userRepository.save(user);

        // 这里需要一个带 currentUserId 的 toDto
        return postConverter.toDto(post, userId);
    }

    public PostDto toggleSavePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Post> savedPosts = user.getSavedPosts();

        if (savedPosts.contains(post)) {
            // 已收藏 -> 取消收藏
            savedPosts.remove(post);
            post.getSavedByUsers().remove(user);
            post.setSavedCount(post.getSavedCount() - 1);
        } else {
            // 未收藏 -> 收藏
            savedPosts.add(post);
            post.getSavedByUsers().add(user);
            post.setSavedCount(post.getSavedCount() + 1);
        }

        // 保存
        postRepository.save(post);
        userRepository.save(user);

        return postConverter.toDto(post);
    }

    @Transactional
    public byte[] getImageDataByOid(Long oid) {
        try (Connection conn = dataSource.getConnection()) {
            // 注：Spring @Transactional 默认已关了 autoCommit，
            // 但仍可手动 setAutoCommit(false) 以防万一
            conn.setAutoCommit(false);

            PGConnection pgConn = conn.unwrap(PGConnection.class);
            LargeObjectManager lobj = pgConn.getLargeObjectAPI();

            // 打开 OID 对应的大对象 (只读)
            LargeObject lo = lobj.open(oid, LargeObjectManager.READ);
            int size = (int) lo.size();
            byte[] data = new byte[size];
            lo.read(data, 0, size);
            lo.close();

            // 不要忘了提交事务
            conn.commit();

            return data;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read large object OID=" + oid, e);
        }
    }

    @Transactional
    public Long createComment(CreateCommentDto commentDto){

        Comment comment = new Comment();
        comment.setComment(commentDto.getComment());

        User user = userRepository.findById(commentDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        comment.setUser(user);

        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        comment.setPost(post);

        comment.setLikeCount(0);
        comment.setTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        Comment saved = commentRepository.save(comment);

        return comment.getId();

    }

	@Override
	public Page<Post> searchPostByFilter(String status, Pageable pageable) {
		// TODO Auto-generated method stub
		Page<Post> postList=postRepository.searchPostByFilter(status, pageable);
		return postList;
	}
    @Override
    public Map<Integer, Long> getTodayPostActivity() {
        // Get today's start and end time
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.with(LocalTime.MIN);
        LocalDateTime endOfDay = now.with(LocalTime.MAX);

        // Get all posts for today
        List<Post> todayPosts = postRepository.findByPostTimeBetween(startOfDay, endOfDay);

        // Group posts by hour
        Map<Integer, Long> hourlyActivity = new HashMap<>();
        for (Post post : todayPosts) {
            int hour = post.getPostTime().getHour();
            hourlyActivity.merge(hour, 1L, Long::sum);
        }

        return hourlyActivity;
    }

    @Override
    public Post save(Post post) {
        // TODO Auto-generated method stub
        Post p=postRepository.save(post);
        return p;
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        Page<Post> postList=postRepository.findAll(pageable);
        return postList;
    }

	@Override
	public Post findById(Long id) {
		// TODO Auto-generated method stub
		Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
		return post;
	}

    public Long saveImageToDatabase(byte[] imageBytes) {
        return jdbcTemplate.execute((Connection connection) -> {
            // 获取 Large Object Manager
            LargeObjectManager lobj = connection.unwrap(org.postgresql.PGConnection.class).getLargeObjectAPI();

            // 创建 Large Object，并返回 OID
            long oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);

            // 打开 Large Object
            LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);

            // 写入数据
            obj.write(imageBytes);
            obj.close();

            System.out.println("Successfully inserted image, OID: " + oid);
            return oid;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getAllPostsWithFilters(
            String status,
            String location,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {

        return postRepository.findAllWithFilters(status, location, startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPostsWithDetails() {
        return postRepository.findAllWithDetails();
    }

    public List<Post> getPostsByIds(List<Long> postIds) {
        return postRepository.findAllByPostIds(postIds);
    }

    @Override
    @Transactional
    public List<Post> findByPostTimeBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return postRepository.findByPostTimeBetween(startDate, endDate);
    }
}
