package ekol.kartoteks.repository;


import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.CompanyEmployeeRelation;
import ekol.kartoteks.domain.EmployeeCustomerRelation;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;


/**
 * Created by kilimci on 13/06/16.
 */
public interface CompanyEmployeeRelationRepository extends ApplicationRepository<CompanyEmployeeRelation> {

    @EntityGraph(value = "CompanyEmployeeRelation.company", type = EntityGraph.EntityGraphType.LOAD)
    List<CompanyEmployeeRelation> findByEmployeeAccountAndRelation(String employeeAccount, EmployeeCustomerRelation relation);

}
