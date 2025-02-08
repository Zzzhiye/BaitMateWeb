package baitmate.ImplementationMethod;

import baitmate.DTO.CatchRecordDTO;
import baitmate.Repository.CatchRecordRepository;
import baitmate.Repository.FishRepository;
import baitmate.Repository.FishingLocationRepository;
import baitmate.Repository.UserRepository;
import baitmate.Service.CatchRecordService;
import baitmate.model.CatchRecord;
import baitmate.model.Fish;
import baitmate.model.FishingLocation;
import baitmate.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatchRecordServiceImpl implements CatchRecordService {

    @Autowired
    private CatchRecordRepository catchRecordRepository;

    @Autowired
    private FishRepository fishRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FishingLocationRepository locationRepository;

    @Override
    public List<CatchRecord> findTopCatchesByWeight(Pageable pageable) {
        return catchRecordRepository.findTopCatchesByWeight(pageable);
    }

    @Override
    public List<CatchRecord> findTopCatchesByLength(Pageable pageable) {
        return catchRecordRepository.findTopCatchesByLength(pageable);
    }

    @Override
    public long countByTimeBetween(@Param("startDate") String startDate, @Param("endDate") String endDate) {
        return catchRecordRepository.countByTimeBetween(startDate, endDate);
    }

    @Override
    public long calculateAverageCatchesPerUser() {
        return catchRecordRepository.calculateAverageCatchesPerUser();
    }

    @Override
    public List<Object[]> findTodayMostCaughtFishWithLocation(@Param("today") String today) {
        return catchRecordRepository.findTodayMostCaughtFishWithLocation(today);
    }

    @Override
    @Transactional
    public void addCatchRecord(CatchRecordDTO catchRecordDTO) {
        CatchRecord catchRecord = new CatchRecord();
        catchRecord.setTime(catchRecordDTO.getTime().toString());
        catchRecord.setWeight(catchRecordDTO.getWeight());
        catchRecord.setLength(catchRecordDTO.getLength());
        catchRecord.setLatitude(catchRecordDTO.getLatitude());
        catchRecord.setLongitude(catchRecordDTO.getLongitude());
        catchRecord.setRemark(catchRecordDTO.getRemark());
        catchRecord.setImage(catchRecordDTO.getImage());
        Fish fish = fishRepository.findById(catchRecordDTO.getFishId())
                .orElseThrow(() -> new RuntimeException("Fish not found"));
        catchRecord.setFish(fish);

        User user = userRepository.findById(catchRecordDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        catchRecord.setUser(user);

        FishingLocation location = locationRepository.findById(catchRecordDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        catchRecord.setFishingLocation(location);

        catchRecordRepository.save(catchRecord);
    }
}
