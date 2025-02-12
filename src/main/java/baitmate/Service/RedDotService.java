package baitmate.Service;

import java.time.LocalDateTime;

public interface RedDotService {
    void createCommentRedDot(Long senderId, Long postId, String commentText, LocalDateTime time);
    void createLikeRedDot(Long senderId, Long postId, LocalDateTime time);
}
