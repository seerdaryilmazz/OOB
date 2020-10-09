package ekol.location.service;

import ekol.location.domain.Region;
import ekol.location.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ozer on 13/12/16.
 */
@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public Iterable<Region> findAll() {
        return regionRepository.findAllByOrderByNameAsc();
    }
}
