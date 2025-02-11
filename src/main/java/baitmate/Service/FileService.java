package baitmate.Service;

import baitmate.model.FileEntity;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
  FileEntity saveFile(MultipartFile file);

  List<FileEntity> getAllFiles();
}
