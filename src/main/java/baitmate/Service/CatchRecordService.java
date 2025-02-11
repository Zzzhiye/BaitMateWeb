package baitmate.Service;

import baitmate.DTO.CatchRecordDTO;
import baitmate.model.CatchRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CatchRecordService {
        List<CatchRecord> findTopCatchesByWeight(Pageable pageable);
        List<CatchRecord> findTopCatchesByLength(Pageable pageable);    long countByTimeBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);
        Double calculateAverageCatchesPerUser();
        List<Object[]> findTodayMostCaughtFishWithLocation(@Param("today") String today);
        List<CatchRecord>findByUserId(Long userId);
        public void addCatchRecord(CatchRecordDTO catchRecord);
}
