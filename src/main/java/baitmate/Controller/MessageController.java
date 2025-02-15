package baitmate.Controller;

import baitmate.DTO.MessageDTO;
import baitmate.Service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller 
@RequestMapping("/admin")
public class MessageController {

  private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
  @Autowired private MessageService messageService;

  @GetMapping("/Message") 
  public String getMessagePage() {
    return "message"; 
  }

  @PostMapping("/send")
  @ResponseBody 
  public ResponseEntity<String> sendAnnouncement(
      @RequestParam("sender") String sender,
      @RequestParam(value = "recipient", required = false)
          String recipient, 
      @RequestParam("subject") String subject,
      @RequestParam("text") String text,
      @RequestParam("announcementType") String announcementType,
      @RequestParam("attachments") MultipartFile[] attachments) {

    try {
      
      MessageDTO messageDTO = new MessageDTO();
      messageDTO.setSender(sender);
      messageDTO.setRecipient(recipient);
      messageDTO.setSubject(subject);
      messageDTO.setText(text);
      messageDTO.setAnnouncementType(announcementType);
      

      messageService.sendAnnouncement(messageDTO, attachments);
      return ResponseEntity.ok("<p style='color:green;'>Announcement sent successfully!</p>");

    } catch (Exception e) {
      logger.error("sendAnnouncement error: ", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("<p style='color:red;'>Failed to send announcement: " + e.getMessage() + "</p>");
    }
  }

  @GetMapping("/notifications")
  @ResponseBody
  public ResponseEntity<?> getNotifications(@RequestParam String username) {
    try {
      return ResponseEntity.ok(messageService.getNotifications(username));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to get notifications: " + e.getMessage());
    }
  }

  @GetMapping("/notifications/unread")
  @ResponseBody
  public ResponseEntity<?> getUnreadNotifications(@RequestParam String username) {
    try {
      return ResponseEntity.ok(messageService.getUnreadNotifications(username));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to get unread notifications: " + e.getMessage());
    }
  }

  @PostMapping("/notifications/read/{id}")
  @ResponseBody
  public ResponseEntity<String> markNotificationAsRead(@PathVariable("id") Long notificationId) {
    try {
      messageService.markNotificationAsRead(notificationId);
      return ResponseEntity.ok("Notification marked as read");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to mark notification as read: " + e.getMessage());
    }
  }
}
