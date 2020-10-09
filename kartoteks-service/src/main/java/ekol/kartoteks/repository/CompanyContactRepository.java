package ekol.kartoteks.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.CompanyContact;

public interface CompanyContactRepository extends ApplicationRepository<CompanyContact> {

    Set<CompanyContact> findByCompanyId(Long companyId);
    long countByIdAndCompanyLocationId(Long id, Long companyLocationId);
    long countByIdAndActiveTrue(Long id);
    
    Set<CompanyContact> findByCompanyLocationId(Long companylocationId);

    @Query("select cc.id from CompanyContact cc where cc.active = true and cc.id in (:ids)")
    List<Long> findIdsByActiveTrueAndIdIn(@Param("ids") List<Long> ids);

    @Query("select cc from CompanyContact cc where cc.id in (:contactIds)")
    List<CompanyContact> findByCompanyIds(@Param("contactIds") List<Long> contactIds);
    
    @EntityGraph(value = "CompanyContact.withCompany", type = EntityGraph.EntityGraphType.LOAD)
    List<CompanyContact> findByEmailsEmailEmailAddress(String emailAddress);

}
