package baitmate.Controller;

import baitmate.DTO.CatchRecordDTO;
import baitmate.Service.CatchRecordService;
import baitmate.model.CatchRecord;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catch-records")
public class CatchRecordController {

  @Autowired private CatchRecordService catchRecordService;

  @PostMapping("/add")
  public void addCatchRecord(@RequestBody CatchRecordDTO catchRecord) {
    catchRecordService.addCatchRecord(catchRecord);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<?> getCatchRecordsByUserId(@PathVariable Long userId) {
    try {
      List<CatchRecord> catchRecords = catchRecordService.findByUserId(userId);
      return ResponseEntity.ok(catchRecords);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.notFound().build();
    }
  }
}
