package baitmate.ImplementationMethod;

import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import baitmate.Repository.ImageRepository;
import baitmate.Service.ImageService;
import baitmate.model.Image;
import jakarta.transaction.Transactional;

@Service
public class ImageServiceImpl implements ImageService {
	@Autowired
	ImageRepository imageRepo;
	
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
			System.out.println("into getImageByImageId method");
			connection.setAutoCommit(false);
            // Retrieve the image OID from the repository
            Image postImage = imageRepo.findById(imageId).orElseThrow(() -> new RuntimeException("Image not found"));
            System.out.println("into1");
            if (postImage == null) {
                throw new RuntimeException("Image not found for post ID: " + imageId);
            }

            LargeObjectManager lobjManager = connection.unwrap(org.postgresql.PGConnection.class).getLargeObjectAPI();
            System.out.println("into2");
            System.out.println("postImage"+ postImage.getId());
            Long imageOid = postImage.getImage();
            System.out.println("into3");
        	 // Access the Large Object using the OID
            LargeObject largeObject = lobjManager.open(imageOid, LargeObjectManager.READ);

            System.out.println("into4");
            // Read the large object data
            byte[] imageData = largeObject.read((int) largeObject.size());
            System.out.println("into5 getImageByImageId method "+ imageData);
            
            largeObject.close();
            connection.commit();
            return imageData;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}


}
