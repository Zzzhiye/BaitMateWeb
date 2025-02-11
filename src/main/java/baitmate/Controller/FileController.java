package baitmate.Controller;

import baitmate.Service.FileService;
import baitmate.model.FileEntity;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {
  private static final Logger logger = LoggerFactory.getLogger(FileController.class);

  @Value("${file.upload.path}")
  private String uploadPath;

  @Autowired private FileService fileService;

  @GetMapping("/attachments/{filename:.+}")
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
    try {
      // 安全检查：防止目录遍历
      if (filename.contains("..")) {
        return ResponseEntity.badRequest().build();
      }

      Path file = Paths.get(uploadPath).resolve(filename).normalize();
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() && resource.isReadable()) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
      } else {
        logger.warn("File not found: {}", filename);
        return ResponseEntity.notFound().build();
      }
    } catch (MalformedURLException e) {
      logger.error("Malformed URL for file: " + filename, e);
      return ResponseEntity.badRequest().build();
    } catch (IOException e) {
      logger.error("Error reading file: " + filename, e);
      return ResponseEntity.internalServerError().build();
    }
  }

  // 新增上传接口，基于 FileService 保存文件，返回文件元数据
  @PostMapping("/upload")
  public ResponseEntity<FileEntity> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      FileEntity savedFile = fileService.saveFile(file);
      return ResponseEntity.ok(savedFile);
    } catch (Exception e) {
      logger.error("Failed to upload file: {}", file.getOriginalFilename(), e);
      return ResponseEntity.internalServerError().build();
    }
  }
}
