package baitmate.Service;

import baitmate.DTO.CatchRecordDTO;
import baitmate.Repository.CatchRecordRepository;
import baitmate.model.CatchRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface CatchRecordService {
        List<CatchRecord> findTopCatchesByWeight(Pageable pageable);
        List<CatchRecord> findTopCatchesByLength(Pageable pageable);    long countByTimeBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);
        long calculateAverageCatchesPerUser();
        List<Object[]> findTodayMostCaughtFishWithLocation(@Param("today") String today);

        public void addCatchRecord(CatchRecordDTO catchRecord);
}
