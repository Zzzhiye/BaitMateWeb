package baitmate.Service;

import baitmate.ImplementationMethod.FishingLocationInterface;
import baitmate.Repository.FishingLocationRepository;
import baitmate.model.FishingLocation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FishingLocationImpl implements FishingLocationInterface {
    @Autowired
    FishingLocationRepository fishinglocationRepo;

    public List<FishingLocation> findAllLocations() {
        return fishinglocationRepo.findAll();
    }
}
