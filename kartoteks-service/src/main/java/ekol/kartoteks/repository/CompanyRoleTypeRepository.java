package ekol.kartoteks.repository;


import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.kartoteks.domain.CompanyRoleType;

/**
 * Created by kilimci on 24/06/16.
 */

public interface CompanyRoleTypeRepository extends LookupRepository<CompanyRoleType> {

    public CompanyRoleType findOneByCode(String code);
}
