package baitmate.converter;

import baitmate.DTO.CommentDto;
import baitmate.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {
  @Autowired private UserConverter userConverter;

  public CommentDto toDto(Comment comment) {
    if (comment == null) return null;
    CommentDto dto = new CommentDto();
    dto.setId(comment.getId());
    dto.setComment(comment.getComment());
    dto.setTime(comment.getTime());
    dto.setLikeCount(comment.getLikeCount());
    // user
    dto.setUser(userConverter.toDto(comment.getUser()));
    return dto;
  }

  public Comment toEntity(CommentDto dto) {
    if (dto == null) return null;
    Comment comment = new Comment();
    comment.setId(dto.getId());
    comment.setComment(dto.getComment());
    comment.setTime(dto.getTime());
    comment.setLikeCount(dto.getLikeCount());
    // user 需要再查数据库 or 根据 dto.getUser().getId() 赋值
    comment.setUser(userConverter.toEntity(dto.getUser()));
    return comment;
  }
}
