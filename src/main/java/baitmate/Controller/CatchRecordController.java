package baitmate.Controller;

import baitmate.DTO.CatchRecordDTO;
import baitmate.DTO.ProfileCatchDTO;
import baitmate.Repository.CatchRecordRepository;
import baitmate.Service.CatchRecordService;
import baitmate.model.CatchRecord;
import baitmate.model.Fish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/catch-records")
public class CatchRecordController {

    @Autowired
    private CatchRecordService catchRecordService;


    @PostMapping("/add")
    public void addCatchRecord(@RequestBody CatchRecordDTO catchRecord) {
        catchRecordService.addCatchRecord(catchRecord);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCatchRecordsByUserId(@PathVariable Long userId) {
        try {
            List<ProfileCatchDTO> catchRecords = catchRecordService.findByUserId(userId);
            return ResponseEntity.ok(catchRecords);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getCatchImage(@PathVariable Long id) {
        Optional<CatchRecord> catchOptional = catchRecordService.findById(id);
        if (catchOptional.isPresent()) {
            CatchRecord catchRecord = catchOptional.get();
            byte[] image = catchRecord.getImage();
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