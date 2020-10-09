package ekol.kartoteks.repository;


import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.*;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.Company;

/**
 * Created by fatmaozyildirim on 3/16/16.
 */
public interface CompanyRepository extends ApplicationRepository<Company> {


    @EntityGraph(value = "Company.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Company findById(Long id);

    Company findNoDetailsById(Long id);

    @EntityGraph(value = "Company.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Stream<Company> findByNameNotNull();

    List<Company> findByLogoUrlNotNull();

    List<Company> findByOwnedByEkol(boolean ownedByEkol);

    Company findByName(String name);
    Company findByLocalName(String localName);
    Company findByCountryIdAndTaxId(Long countryId, String taxId);
    Company findByCountryIdAndTaxIdAndIdNot(Long countryId, String taxId, Long id);
    List <Company> findByEoriNumber(String eoriNumber);
    List<Company> findByTaxOfficeCodeAndTaxId(String taxOfficeCode, String taxId);
    List<Company> findByTckn(String tckn);

    @Query(value = "select count(c) from Company c where c.deleted = 0")
    Long countAll();

    @Query(value = "select count(distinct cl.company) from CompanyLocation cl where cl.deleted = 0")
    Long countCompaniesWithLocation();

    @Query(value = "select count(distinct cc.company) from CompanyContact cc where cc.deleted = 0")
    Long countCompaniesWithContact();

    Company findByShortNameAndIdNot(String shortName, Long id);
    Company findByShortName(String shortName);
    List<Company> findByShortNameIsNull();

    List<Company> findByIdIn(List<Long> ids);
    
    List<Company> findByWebsiteEndingWithOrEmailDomain(String website, String emailDomain);

}
