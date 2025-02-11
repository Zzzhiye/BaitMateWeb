package baitmate.Service;

import baitmate.model.Image;
import java.util.List;

public interface ImageService {

  byte[] getImageByImageId(Long imageId);

  List<Image> getImageByPostId(Long postId);
}
