package baitmate.ImplementationMethod;

import baitmate.Repository.ImageRepository;
import baitmate.Service.ImageService;
import baitmate.model.Image;
import jakarta.transaction.Transactional;
import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
  @Autowired ImageRepository imageRepo;

  private final DataSource dataSource;

  public ImageServiceImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  @Transactional
  public List<Image> getImageByPostId(Long postId) {
    List<Image> imageIds = imageRepo.findByPostId(postId);
    return imageIds;
  }

  @Override
  @Transactional
  public byte[] getImageByImageId(Long imageId) {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      
      Image postImage =
          imageRepo.findById(imageId).orElseThrow(() -> new RuntimeException("Image not found"));
      if (postImage == null) {
        throw new RuntimeException("Image not found for post ID: " + imageId);
      }

      LargeObjectManager lobjManager =
          connection.unwrap(org.postgresql.PGConnection.class).getLargeObjectAPI();
      Long imageOid = postImage.getImage();
      
      LargeObject largeObject = lobjManager.open(imageOid, LargeObjectManager.READ);
      
      byte[] imageData = largeObject.read(largeObject.size());

      largeObject.close();
      connection.commit();
      return imageData;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
