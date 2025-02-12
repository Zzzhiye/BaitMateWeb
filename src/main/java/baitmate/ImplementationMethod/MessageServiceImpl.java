package baitmate.ImplementationMethod;

import baitmate.DTO.MessageDTO;
import baitmate.Repository.MessageRepository;
import baitmate.Repository.UserRepository;
import baitmate.Repository.NotificationRepository;
import baitmate.Service.EmailService;
import baitmate.Service.MessageService;
import baitmate.model.Message;
import baitmate.model.User;
import baitmate.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

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
    public void init() {  // Removed @Override as it's not from the interface
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

    @Override
    @Transactional
    public void sendAnnouncement(MessageDTO messageDTO, MultipartFile[] attachments) {
        Message message = null;
        try {
            List<String> savedFileNames = handleAttachments(attachments);
            message = createMessage(messageDTO, savedFileNames);
            message = messageRepository.save(message);

            if ("send to all users".equals(messageDTO.getAnnouncementType())) {
                sendToAllUsers(messageDTO, savedFileNames);
            } else {
                sendToSingleUser(messageDTO, savedFileNames);
            }
        } catch (Exception e) {
            log.error("Error in sendAnnouncement", e);
            if (message != null && message.getAttachmentFileNames() != null) {
                cleanupAttachments(message.getAttachmentFileNames());
            }
            throw new RuntimeException("Failed to send announcement", e);
        }
    }

    @Override
    public List<Notification> getNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return notificationRepository.findByUser(user);
    }

    @Override
    public List<Notification> getUnreadNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return notificationRepository.findByUserAndIsRead(user, false);
    }

    @Override
    @Transactional
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    private List<String> handleAttachments(MultipartFile[] attachments) throws IOException {
        List<String> savedFiles = new ArrayList<>();
        if (attachments != null) {
            for (MultipartFile file : attachments) {
                if (file != null && !file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename == null) {
                        throw new IllegalArgumentException("Original filename is null");
                    }
                    String fileName = UUID.randomUUID() + "_" + originalFilename;
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
        message.setAttachmentFileNames(savedFileNames);
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
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(subject);
        notification.setContent(text);
        notification.setCreateTime(LocalDateTime.now());
        notification.setRead(false);
        notification.setAttachmentPaths(attachmentFileNames);
        notificationRepository.save(notification);

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
                                    Files.readAllBytes(file.toPath()) // No need for throws IOException here
                            );
                        } else {
                            log.warn("Attachment file not found: {}", fileName);
                        }
                    }
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

    private static class MockMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        public MockMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = Objects.requireNonNull(name, "Name cannot be null");
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = Objects.requireNonNull(content, "Content cannot be null");
        }

        @Override
        @NonNull
        public String getName() { return name; }

        @Override
        public String getOriginalFilename() { return originalFilename; }

        @Override
        public String getContentType() { return contentType; }

        @Override
        public boolean isEmpty() { return content.length == 0; }

        @Override
        public long getSize() { return content.length; }

        @Override
        @NonNull
        public byte[] getBytes() { return content; }

        @Override
        @NonNull
        public InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            Files.write(dest.toPath(), content);
        }
    }
}
