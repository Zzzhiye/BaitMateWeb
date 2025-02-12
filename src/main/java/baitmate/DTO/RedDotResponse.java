package baitmate.DTO;

import baitmate.model.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedDotResponse {
    private Long id;
    private Long senderId; // 发送通知的用户（完整对象）
    private Long postId;   // 关联的 Post（完整对象）
    private LocalDateTime time;
    private RedDotType type;
    private String commentText; // 仅 `CommentNotification` 需要

    public RedDotResponse(RedDot redDot) {
        this.id = redDot.getId();
        this.senderId = redDot.getSender().getId(); // 直接返回完整 User
        this.postId = redDot.getPost().getId();     // 直接返回完整 Post
        this.time = redDot.getTime();
        this.type = redDot.getType();

        // 只有评论通知才会有 commentText
        if (redDot instanceof CommentRedDot) {
            this.commentText = ((CommentRedDot) redDot).getCommentText();
        } else {
            this.commentText = null; // 点赞通知不需要 commentText
        }
    }
}

