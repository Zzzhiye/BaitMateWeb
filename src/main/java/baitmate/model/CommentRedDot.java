package baitmate.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CommentRedDot extends RedDot {
    private String commentText;

    public CommentRedDot() {
        setType(RedDotType.COMMENT);
    }

    @Override
    public String getMessage() {
        return getSender().getUsername() + " commented your post: " + getPost().getPostTitle();
    }
}

