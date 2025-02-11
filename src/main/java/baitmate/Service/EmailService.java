package baitmate.Service;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);

    // 添加发送带附件邮件的方法
    void sendMessageWithAttachments(String to, String subject, String text, MultipartFile[] attachments);
}