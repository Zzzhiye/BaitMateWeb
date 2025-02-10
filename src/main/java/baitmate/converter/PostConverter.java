package baitmate.converter;

import baitmate.DTO.CommentDto;
import baitmate.DTO.ImageDto;
import baitmate.DTO.PostDto;
import baitmate.Repository.UserRepository;
import baitmate.model.Image;
import baitmate.model.Post;
import baitmate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostConverter {

    @Autowired
    private UserConverter userConverter;
    @Autowired
    private CommentConverter commentConverter;
    @Autowired
    private UserRepository userRepository;

    public PostDto toDto(Post post) {
        if (post == null) return null;
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setPostTitle(post.getPostTitle());
        dto.setPostContent(post.getPostContent());
        dto.setPostStatus(post.getPostStatus());
        dto.setPostTime(post.getPostTime());
        dto.setLikeCount(post.getLikeCount());
        dto.setSavedCount(post.getSavedCount());
        dto.setAccuracyScore(post.getAccuracyScore());
        dto.setLocation(post.getLocation());

        // user
        dto.setUser(userConverter.toDto(post.getUser()));

        // 若 post 有 fishingLocation
//        if (post.getFishingLocation() != null) {
//            dto.setFishingLocationId(post.getFishingLocation().getId());
//            dto.setFishingLocationName(post.getFishingLocation().getLocationName());
//        }

        // comments
        if (post.getComments() != null) {
            List<CommentDto> cmtList = post.getComments().stream()
                    .map(commentConverter::toDto)
                    .collect(Collectors.toList());
            dto.setComments(cmtList);
        }

        // images
        // images -> List<ImageDto>
        if (post.getImages() != null) {
            List<ImageDto> imageDtos = post.getImages().stream()
                    .map(this::imageToDto)
                    .collect(Collectors.toList());
            dto.setImages(imageDtos);
        }
        return dto;
    }

    public PostDto toDto(Post post, Long currentUserId) {
        if (post == null) return null;
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setPostTitle(post.getPostTitle());
        dto.setPostContent(post.getPostContent());
        dto.setPostStatus(post.getPostStatus());
        dto.setPostTime(post.getPostTime());
        dto.setLikeCount(post.getLikeCount());
        dto.setSavedCount(post.getSavedCount());
        dto.setAccuracyScore(post.getAccuracyScore());
        dto.setLocation(post.getLocation());

        // user
        dto.setUser(userConverter.toDto(post.getUser()));

        // comments
        if (post.getComments() != null) {
            List<CommentDto> cmtList = post.getComments().stream()
                    .map(commentConverter::toDto)
                    .collect(Collectors.toList());
            dto.setComments(cmtList);
        }

        // images
        // images -> List<ImageDto>
        if (post.getImages() != null) {
            List<ImageDto> imageDtos = post.getImages().stream()
                    .map(this::imageToDto)
                    .collect(Collectors.toList());
            dto.setImages(imageDtos);
        }

        if (currentUserId != null) {
            userRepository.findById(currentUserId).ifPresentOrElse(user -> {
                dto.setLikedByCurrentUser(post.getLikedByUsers().contains(user));
                dto.setSavedByCurrentUser(post.getSavedByUsers().contains(user));
            }, () -> {
                dto.setLikedByCurrentUser(false);
                dto.setSavedByCurrentUser(false);
            });
        } else {
            dto.setLikedByCurrentUser(false);
            dto.setSavedByCurrentUser(false);
        }


        return dto;
    }

    public Post toEntity(PostDto dto) {
        if (dto == null) return null;
        Post post = new Post();
        post.setId(dto.getId());
        post.setPostTitle(dto.getPostTitle());
        post.setPostContent(dto.getPostContent());
        post.setPostStatus(dto.getPostStatus());
        post.setPostTime(dto.getPostTime());
        post.setLikeCount(dto.getLikeCount());
        post.setSavedCount(dto.getSavedCount());
        post.setAccuracyScore(dto.getAccuracyScore());
        post.setLocation(dto.getLocation());
        // user 需要再查数据库或根据 dto.getUser().getId() setUser
        post.setUser(userConverter.toEntity(dto.getUser()));
        // comments, images 一般新增Post时不一定包含，需要看业务需求
        return post;
    }

    // Image -> ImageDto (把 byte[] 转成 Base64)
    private ImageDto imageToDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setImage(image.getImage()); // 这里就是 entity 的 String image
        return dto;
    }

    // ImageDto -> Image (把 Base64 转回 byte[])
    private Image imageToEntity(ImageDto dto) {
        Image image = new Image();
        image.setId(dto.getId());
        image.setImage(dto.getImage()); // 直接赋值
        return image;
    }

    // 一个方法，供 service 调用，把 List<ImageDto> 转为 List<Image>
    public List<Image> toImageEntityList(List<ImageDto> imageDtos, Post post) {
        if (imageDtos == null) return Collections.emptyList();
        return imageDtos.stream()
                .map(imageDto -> {
                    Image image = imageToEntity(imageDto);
                    // 这里要手动关联回去
                    image.setPost(post);
                    return image;
                })
                .collect(Collectors.toList());
    }
}

