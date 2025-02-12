package baitmate.ImplementationMethod;

import baitmate.Repository.RedDotRepository;
import baitmate.Repository.PostRepository;
import baitmate.Repository.UserRepository;
import baitmate.Service.RedDotService;
import baitmate.model.CommentRedDot;
import baitmate.model.LikeRedDot;
import baitmate.model.Post;
import baitmate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RedDotServiceImpl implements RedDotService {

    @Autowired
    private RedDotRepository redDotRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public void createCommentRedDot(Long senderId, Long postId, String commentText, LocalDateTime time) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        CommentRedDot redDot = new CommentRedDot();
        redDot.setReceiver(post.getUser());
        redDot.setSender(sender);
        redDot.setPost(post);
        redDot.setCommentText(commentText);
        redDot.setTime(time);

        redDotRepository.save(redDot);
    }

    public void createLikeRedDot(Long senderId, Long postId, LocalDateTime time) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        LikeRedDot redDot = new LikeRedDot();
        redDot.setReceiver(post.getUser());
        redDot.setSender(sender);
        redDot.setPost(post);
        redDot.setTime(time);

        redDotRepository.save(redDot);
    }
}
