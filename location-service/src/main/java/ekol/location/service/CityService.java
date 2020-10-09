package ekol.location.service;

import ekol.location.domain.City;
import ekol.location.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ozer on 13/12/16.
 */
@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public Iterable<City> findAll() {
        return cityRepository.findAllByOrderByNameAsc();
    }
}
