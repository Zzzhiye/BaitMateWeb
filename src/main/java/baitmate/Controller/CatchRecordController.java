package baitmate.Controller;

import baitmate.DTO.CatchRecordDTO;
import baitmate.Service.CatchRecordService;
import baitmate.model.CatchRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catch-records")
public class CatchRecordController {

    @Autowired
    private CatchRecordService catchRecordService;

    @PostMapping("/add")
    public void addCatchRecord(@RequestBody CatchRecordDTO catchRecord) {
        catchRecordService.addCatchRecord(catchRecord);
    }
}
