package baitmate.Service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import baitmate.model.FileEntity;

public interface FileService {
    FileEntity saveFile(MultipartFile file);
    List<FileEntity> getAllFiles();
}
