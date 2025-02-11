package baitmate.Repository;

import baitmate.model.Notification;
import baitmate.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> findByUser(User user);

  List<Notification> findByUserAndIsRead(User user, boolean isRead);
}
