package baitmate.Service;

import java.util.List;

import baitmate.model.Image;

public interface ImageService {
	
	byte[] getImageByImageId(Long imageId);
	
	List<Image> getImageByPostId(Long postId);

}
