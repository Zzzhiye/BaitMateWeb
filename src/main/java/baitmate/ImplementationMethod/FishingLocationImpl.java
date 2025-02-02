package baitmate.ImplementationMethod;

import baitmate.Repository.FishingLocationRepository;
import baitmate.Service.FishingLocationService;
import baitmate.model.FishingLocation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FishingLocationImpl implements FishingLocationService {
    @Autowired
    FishingLocationRepository fishinglocationRepo;

    public List<FishingLocation> findAllLocations() {
        return fishinglocationRepo.findAll();
    }
}
