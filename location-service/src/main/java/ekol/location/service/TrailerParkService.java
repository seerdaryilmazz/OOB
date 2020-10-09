package ekol.location.service;

import ekol.location.domain.location.trailerPark.TrailerPark;
import ekol.location.repository.LocationRepository;
import ekol.location.repository.TrailerParkRepository;
import ekol.location.validator.TrailerParkValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kilimci on 02/05/2017.
 */
@Service
public class TrailerParkService {

    @Autowired
    private TrailerParkValidator trailerParkValidator;

    @Autowired
    private EstablishmentService establishmentService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TrailerParkRepository trailerParkRepository;

    @Transactional
    public TrailerPark save(TrailerPark trailerPark){
        trailerParkValidator.validate(trailerPark);
        trailerPark.setEstablishment(establishmentService.save(trailerPark.getEstablishment()));
        trailerPark.getLocation().setTimezone(trailerPark.getCountry().getTimezone());
        trailerPark.setLocation(locationRepository.save(trailerPark.getLocation()));
        TrailerPark saved = trailerParkRepository.save(trailerPark);
        return saved;
    }

    public void delete(Long id){
        TrailerPark trailerPark = trailerParkRepository.findOne(id);
        if(trailerPark != null){
            trailerPark.setDeleted(true);
            trailerParkRepository.save(trailerPark);
        }
    }
}
