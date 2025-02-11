package baitmate.Service;

import baitmate.DTO.MessageDTO;
import baitmate.Repository.MessageRepository;
import baitmate.Repository.UserRepository;
import baitmate.Repository.NotificationRepository;
import baitmate.model.Message;
import baitmate.model.User;
import baitmate.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Value("${file.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        try {
            Path path = Paths.get(uploadPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            log.info("Upload directory initialized at: {}", uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    @Transactional // Make the entire sendAnnouncement method transactional
    public void sendAnnouncement(MessageDTO messageDTO, MultipartFile[] attachments) {
        Message message = null;
        try {
            // 1. Handle Attachments (if any)
            List<String> savedFileNames = handleAttachments(attachments);

            // 2. Create Message Entity
            message = createMessage(messageDTO, savedFileNames);
            message = messageRepository.save(message); // Save the message *before* sending emails/notifications

            // 3. Send Notifications and Emails
            if ("send to all users".equals(messageDTO.getAnnouncementType())) {
                sendToAllUsers(messageDTO, savedFileNames);
            } else {
                sendToSingleUser(messageDTO, savedFileNames);
            }

        } catch (Exception e) {
            log.error("Error in sendAnnouncement", e);
            // Cleanup attachments if something goes wrong *after* they've been saved
            if (message != null && message.getAttachmentFileNames() != null) {
                cleanupAttachments(message.getAttachmentFileNames());
            }

            throw new RuntimeException("Failed to send announcement", e);
        }
    }
    private List<String> handleAttachments(MultipartFile[] attachments) throws IOException {
        List<String> savedFiles = new ArrayList<>();
        if (attachments != null) {
            for (MultipartFile file : attachments) {
                if (file != null && !file.isEmpty()) { // Check for null and emptiness
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename == null) {
                        throw new IllegalArgumentException("Original filename is null"); // Or handle it more gracefully
                    }
                    String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                    Path targetLocation = Paths.get(uploadPath).resolve(fileName);
                    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                    savedFiles.add(fileName);
                    log.info("Saved file: {} ({} bytes)", fileName, file.getSize());
                }
            }
        }
        return savedFiles;
    }


    private Message createMessage(MessageDTO messageDTO, List<String> savedFileNames) {
        Message message = new Message();
        message.setSender(messageDTO.getSender());
        message.setRecipient(messageDTO.getRecipient());
        message.setSubject(messageDTO.getSubject());
        message.setText(messageDTO.getText());
        message.setAnnouncementType(messageDTO.getAnnouncementType());
        message.setAttachmentFileNames(savedFileNames); // Set the saved filenames
        return message;
    }

    private void sendToAllUsers(MessageDTO messageDTO, List<String> savedFileNames) {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            createNotificationAndSendEmail(user, messageDTO.getSubject(), messageDTO.getText(), savedFileNames);
        }
    }

    private void sendToSingleUser(MessageDTO messageDTO, List<String> savedFileNames) {
        User user = userRepository.findByUsername(messageDTO.getRecipient())
                .orElseThrow(() -> new RuntimeException("User not found: " + messageDTO.getRecipient()));
        createNotificationAndSendEmail(user, messageDTO.getSubject(), messageDTO.getText(), savedFileNames);
    }

    private void createNotificationAndSendEmail(User user, String subject, String text, List<String> attachmentFileNames) {
        // Save notification
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(subject);
        notification.setContent(text);
        notification.setCreateTime(LocalDateTime.now());
        notification.setRead(false);
        notification.setAttachmentPaths(attachmentFileNames);
        notificationRepository.save(notification);

        // Send email with attachments if user has email
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            try {
                if (!attachmentFileNames.isEmpty()) {
                    MultipartFile[] multipartFiles = new MultipartFile[attachmentFileNames.size()];
                    for (int i = 0; i < attachmentFileNames.size(); i++) {
                        String fileName = attachmentFileNames.get(i);
                        File file = Paths.get(uploadPath, fileName).toFile();
                        if (file.exists()) {
                            multipartFiles[i] = new MockMultipartFile(
                                    fileName,
                                    fileName,
                                    Files.probeContentType(file.toPath()),
                                    Files.readAllBytes(file.toPath())
                            );
                        } else {
                            log.warn("Attachment file not found: {}", fileName);
                        }
                    }
                    // 过滤掉任何null值
                    multipartFiles = Arrays.stream(multipartFiles)
                            .filter(Objects::nonNull)
                            .toArray(MultipartFile[]::new);

                    if (multipartFiles.length > 0) {
                        emailService.sendMessageWithAttachments(user.getEmail(), subject, text, multipartFiles);
                    } else {
                        emailService.sendSimpleMessage(user.getEmail(), subject, text);
                    }
                } else {
                    emailService.sendSimpleMessage(user.getEmail(), subject, text);
                }
            } catch (Exception e) {
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
            }
        }
    }

    private void cleanupAttachments(List<String> fileNames) {
        if (fileNames != null) {
            for (String fileName : fileNames) {
                try {
                    Files.deleteIfExists(Paths.get(uploadPath).resolve(fileName));
                } catch (IOException e) {
                    log.error("Error cleaning up file: {}", fileName, e);
                }
            }
        }
    }
    // 新增：获取用户通知
    public List<Notification> getNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return notificationRepository.findByUser(user);
    }

    // 新增：获取未读通知
    public List<Notification> getUnreadNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return notificationRepository.findByUserAndIsRead(user, false);
    }

    // 新增：标记通知为已读
    @Transactional
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    // Add this utility class at the bottom of MessageService
    private static class MockMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        public MockMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String getName() { return name; }

        @Override
        public String getOriginalFilename() { return originalFilename; }

        @Override
        public String getContentType() { return contentType; }

        @Override
        public boolean isEmpty() { return content == null || content.length == 0; }

        @Override
        public long getSize() { return content.length; }

        @Override
        public byte[] getBytes() throws IOException { return content; }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            Files.write(dest.toPath(), content);
        }
    }
}

