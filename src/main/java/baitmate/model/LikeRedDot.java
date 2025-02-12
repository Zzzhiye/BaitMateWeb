package baitmate.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class LikeRedDot extends RedDot {

    public LikeRedDot() {
        setType(RedDotType.LIKE);
    }

    @Override
    public String getMessage() {
        return getSender().getUsername() + " liked your post: " + getPost().getPostTitle();
    }
}

