package baitmate.DTO;

import baitmate.model.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedDotResponse {
    private Long id;
    private Long senderId; 
    private Long postId;   
    private LocalDateTime time;
    private RedDotType type;
    private String commentText; 

    public RedDotResponse(RedDot redDot) {
        this.id = redDot.getId();
        this.senderId = redDot.getSender().getId(); 
        this.postId = redDot.getPost().getId();     
        this.time = redDot.getTime();
        this.type = redDot.getType();

        
        if (redDot instanceof CommentRedDot) {
            this.commentText = ((CommentRedDot) redDot).getCommentText();
        } else {
            this.commentText = null; 
        }
    }
}

