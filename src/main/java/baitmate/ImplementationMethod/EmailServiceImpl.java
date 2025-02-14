package baitmate.ImplementationMethod;

import baitmate.Service.EmailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {
  private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

  @Autowired private JavaMailSender emailSender;

  @Value("${spring.mail.username}") // Use the configured username
  private String fromEmail;

  @Override
  public void sendSimpleMessage(String to, String subject, String text) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(fromEmail); // Use the injected fromEmail
      message.setTo(to);
      message.setSubject(subject);
      message.setText(text);

      logger.info(
          "Attempting to send email from: {} to: {} with subject: {}", fromEmail, to, subject);

      emailSender.send(message);
      logger.info("Simple email sent successfully to: {}", to);

    } catch (MailAuthenticationException ex) {
      logger.error("Authentication failure sending email to {}: {}", to, ex.getMessage());
      throw new RuntimeException(
          "Authentication failed: " + ex.getMessage(), ex); // More specific exception
    } catch (MailSendException ex) {
      logger.error("Failed to send email to {}: {}", to, ex.getMessage());
      if (ex.getCause() != null) {
        logger.error("  Caused by: {}", ex.getCause().getMessage()); // Log the cause!
      }
      for (Exception e : ex.getFailedMessages().values()) { // Check failed message
        logger.error("  Failed message: {}", e.getMessage());
      }

      throw new RuntimeException(
          "Failed to send email: " + ex.getMessage(), ex); // More specific exception
    } catch (Exception e) {
      logger.error(
          "Failed to send simple email. From: {}, To: {}, Subject: {}, Error: {}",
          fromEmail,
          to,
          subject,
          e.getMessage()); // Log full stack trace
      throw new RuntimeException("Failed to send email", e);
    }
  }

  @Override
  public void sendMessageWithAttachments(
      String to, String subject, String text, MultipartFile[] attachments) {
    try {
      MimeMessage message = emailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setFrom(fromEmail); // Use the injected fromEmail
      helper.setTo(to);
      helper.setSubject(subject);

      helper.setText(text, true); // Enable HTML content

      if (attachments != null) {
        for (MultipartFile file : attachments) {
          if (file != null && !file.isEmpty()) {

            logger.info(
                "Attaching file: {} (size: {} bytes) for recipient: {}",
                file.getOriginalFilename(),
                file.getSize(),
                to);
            try {
              helper.addAttachment(file.getOriginalFilename(), file);
            } catch (Exception attEx) {
              logger.error(
                  "Error attaching file: {} for recipient: {}. Error: {}",
                  file.getOriginalFilename(),
                  to,
                  attEx.getMessage());
              throw attEx;
            }
          }
        }
      }

      logger.info(
          "Sending email with {} attachment(s) to: {}",
          attachments != null ? attachments.length : 0,
          to);
      emailSender.send(message);
      logger.info("Successfully sent email with attachments to: {}", to);

    } catch (Exception e) {
      logger.error("Failed to send email with attachments to: {}. Error: {}", to, e.getMessage());
      throw new RuntimeException("Failed to send email with attachments", e);
    }
  }
}
