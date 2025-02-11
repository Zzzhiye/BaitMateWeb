package baitmate.Service;

import baitmate.DTO.MessageDTO;
import baitmate.model.Notification;
import jakarta.annotation.PostConstruct;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface MessageService {


    @PostConstruct
    void init();

    void sendAnnouncement(MessageDTO messageDTO, MultipartFile[] attachments);

    List<Notification> getNotifications(String username);

    List<Notification> getUnreadNotifications(String username);

    void markNotificationAsRead(Long notificationId);
}
