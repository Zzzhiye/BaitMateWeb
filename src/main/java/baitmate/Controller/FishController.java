package baitmate.Controller;

import baitmate.Repository.FishRepository;
import baitmate.model.Fish;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fish")
public class FishController {

  @Autowired private FishRepository fishRepository;

  @GetMapping("/image/{id}")
  public ResponseEntity<byte[]> getFishImage(@PathVariable Long id) {
    Optional<Fish> fishOptional = fishRepository.findById(id);
    if (fishOptional.isPresent()) {
      Fish fish = fishOptional.get();
      byte[] image = fish.getFishImage();
      if (image != null) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/jpeg");
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }
}
