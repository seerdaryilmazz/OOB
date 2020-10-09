package ekol.kartoteks.repository;


import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.CompanyRole;
import ekol.kartoteks.domain.CompanyRoleType;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

/**
 * Created by kilimci on 13/06/16.
 */
public interface CompanyRoleRepository extends ApplicationRepository<CompanyRole> {

    List<CompanyRole> findByCompanyId(Long companyId);

    @EntityGraph(value = "CompanyRole.withCompany", type = EntityGraph.EntityGraphType.LOAD)
    List<CompanyRole> findByRoleType(CompanyRoleType roleType);
}
