package baitmate.Service;

import baitmate.DTO.CatchRecordDTO;
import baitmate.DTO.ProfileCatchDTO;
import baitmate.model.CatchRecord;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public interface CatchRecordService {
  List<CatchRecord> findTopCatchesByWeight(Pageable pageable);

  List<CatchRecord> findTopCatchesByLength(Pageable pageable);

  long countByTimeBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

  Double calculateAverageCatchesPerUser();

  List<Object[]> findTodayMostCaughtFishWithLocation(@Param("today") String today);

  List<ProfileCatchDTO> findByUserId(Long userId);

  public void addCatchRecord(CatchRecordDTO catchRecord);

  Optional<CatchRecord> findById(Long id);
}
