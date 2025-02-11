package baitmate.Service.impl;

import baitmate.Repository.FileRepository;
import baitmate.Service.FileService;
import baitmate.model.FileEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

  @Autowired private FileRepository fileRepository; // 如果需要持久化文件元数据

  @Override
  public FileEntity saveFile(MultipartFile file) {
    // 示例：生成实体并保存，实际可以扩展存储文件至磁盘/云等
    FileEntity entity = new FileEntity();
    entity.setFilename(file.getOriginalFilename());
    entity.setFileType(file.getContentType());
    entity.setUploadTime(LocalDateTime.now());
    return fileRepository.save(entity);
  }

  @Override
  public List<FileEntity> getAllFiles() {
    return fileRepository.findAll();
  }
}
