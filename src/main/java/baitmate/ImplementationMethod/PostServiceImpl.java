package baitmate.ImplementationMethod;

import baitmate.DTO.PostDto;
import baitmate.Repository.FishingLocationRepository;
import baitmate.Repository.PostRepository;
import baitmate.Repository.UserRepository;
import baitmate.Service.PostService;
import baitmate.converter.PostConverter;
import baitmate.converter.UserConverter;
import baitmate.model.Image;
import baitmate.model.Post;
import baitmate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    private PostConverter postConverter;
    @Autowired
    private UserConverter userConverter;

    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        Collectors Collectors = null;
        return posts.stream()
                .map(postConverter::toDto)
                .collect(Collectors.toList());
    }

    public PostDto createPost(PostDto postDto) {
        // 1. dto -> entity
        Post post = postConverter.toEntity(postDto);

        // 2. 若前端传了 user 信息
        if (postDto.getUser() != null && postDto.getUser().getId() != null) {
            User user = userRepository.findById(postDto.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            post.setUser(user);
        }
        // 2.1 你也可设置 postTime = LocalDateTime.now() 等
        post.setPostTime(LocalDateTime.now());
        post.setLikeCount(0);
        post.setSavedCount(0);

        post.setPostTitle(postDto.getPostTitle());
        post.setPostContent(postDto.getPostContent());

        // c. 如果前端上传了 fishingLocationId
//        if (postDto.getFishingLocationId() != null) {
//            FishingLocation location = fishingLocationRepository
//                    .findById(postDto.getFishingLocationId())
//                    .orElseThrow(() -> new RuntimeException("Location not found"));
//            post.setFishingLocation(location);
//        }

        // 3. 处理 images
        List<Image> imageEntities = postConverter.toImageEntityList(postDto.getImages(), post);
        post.setImages(imageEntities);

        // 4. save
        Post saved = postRepository.save(post);

        // 5. entity -> dto
        return postConverter.toDto(saved);
    }

    public PostDto updatePost(Long postId, PostDto postDto) {
        // 1. 找到原post
        Post existing = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 2. 更新字段
        existing.setPostTitle(postDto.getPostTitle());
        existing.setPostContent(postDto.getPostContent());
        // ... 其他字段

        // 3. 更新 images
        //    先把旧的 images 清空或删除，再 set 新的？
        //    看业务需求，这里示例直接清空再重新设置
        existing.getImages().clear();

        List<Image> newImages = postConverter.toImageEntityList(postDto.getImages(), existing);
        existing.getImages().addAll(newImages);

        // 4. save
        Post updated = postRepository.save(existing);

        // 5. 返回
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

        // 转换返回
        return postConverter.toDto(post);
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
}
