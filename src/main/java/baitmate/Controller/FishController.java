package baitmate.Controller;

import baitmate.model.Fish;
import baitmate.Repository.FishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/fish")
public class FishController {

    @Autowired
    private FishRepository fishRepository;

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