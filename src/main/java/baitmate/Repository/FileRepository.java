package baitmate.Repository;

import baitmate.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
  // 添加自定义查询方法，如需时：
  // List<FileEntity> findByFilename(String filename);
}
