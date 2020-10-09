package ekol.kartoteks.repository;


import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyRelation;

import java.util.List;

/**
 * Created by kilimci on 24/06/16.
 */
public interface CompanyRelationRepository extends ApplicationRepository<CompanyRelation> {

    List<CompanyRelation> findByActiveCompany(Company activeCompany);
    List<CompanyRelation> findByPassiveCompany(Company passiveCompany);
}
