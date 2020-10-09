package ekol.kartoteks.repository;


import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.CompanySector;

import java.util.List;

/**
 * Created by kilimci on 28/04/16.
 */
public interface CompanySectorRepository extends ApplicationRepository<CompanySector> {

    public List<CompanySector> findByCompanyId(Long companyId);
}
