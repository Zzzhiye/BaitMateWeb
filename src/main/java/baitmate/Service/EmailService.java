package baitmate.Service;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
  void sendSimpleMessage(String to, String subject, String text);

  
  void sendMessageWithAttachments(
      String to, String subject, String text, MultipartFile[] attachments);
}
