package ekol.location.service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.Country;
import ekol.location.domain.location.comnon.Address;
import ekol.location.domain.location.comnon.Establishment;
import ekol.location.domain.location.comnon.workinghour.WorkingHour;
import ekol.location.repository.CountryRepository;
import ekol.location.repository.EstablishmentRepository;
import ekol.location.repository.WorkingHourRepository;
import ekol.location.validator.EstablishmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by kilimci on 28/04/2017.
 */
@Service
public class EstablishmentService {

    @Autowired
    private EstablishmentRepository establishmentRepository;

    @Autowired
    private WorkingHourRepository workingHourRepository;

    @Autowired
    private EstablishmentValidator establishmentValidator;

    @Autowired
    private CountryRepository countryRepository;

    public Establishment save(Establishment establishment){
        establishmentValidator.validate(establishment);
        setCountry(establishment.getAddress());

        Establishment saved = establishmentRepository.save(establishment);
        saveWorkingHours(establishment.getWorkingHours(), saved);

        return saved;
    }

    private void saveWorkingHours(Set<WorkingHour> workingHours, Establishment establishment){
        if(workingHours != null){
            workingHours.forEach(wh -> {
                wh.setEstablishment(establishment);
                workingHourRepository.save(wh);
            });
        }
    }

    private void setCountry(Address address){
        Country country = countryRepository.findByIso(address.getCountry().getIso());
        if (country == null) {
            throw new ResourceNotFoundException("Country '" + address.getCountry().getIso() + "' - '" + address.getCountry().getName() + "' does not exist.");
        }
        address.setCountry(country);
    }
}
