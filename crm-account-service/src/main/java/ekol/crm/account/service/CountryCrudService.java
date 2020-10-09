package ekol.crm.account.service;

import ekol.crm.account.domain.enumaration.CountryStatus;
import ekol.crm.account.domain.model.Country;
import ekol.crm.account.repository.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CountryCrudService {

    private CountryRepository repository;

    public List<Country> list() {
        return repository.findByStatusOrderByRankAscNameAsc(CountryStatus.ACTIVE);
    }

    public Optional<Country> findByIso(String iso) {
        return repository.findByIsoAndStatus(iso, CountryStatus.ACTIVE);
    }

    public Optional<Country> findByName(String name) {
        return repository.findByNameIgnoreCaseAndStatus(name, CountryStatus.ACTIVE);
    }
}



