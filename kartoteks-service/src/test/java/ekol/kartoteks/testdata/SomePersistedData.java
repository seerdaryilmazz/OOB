package ekol.kartoteks.testdata;

import ekol.kartoteks.domain.*;
import ekol.kartoteks.domain.common.Country;
import ekol.kartoteks.repository.*;
import ekol.kartoteks.repository.common.CountryRepository;
import ekol.kartoteks.repository.common.TaxOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 18/10/16.
 */
@Component
public class SomePersistedData {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CompanySegmentTypeRepository companySegmentTypeRepository;

    @Autowired
    private CompanyTypeRepository companyTypeRepository;

    @Autowired
    private TaxOfficeRepository taxOfficeRepository;

    @Autowired
    private SalesPortfolioRepository salesPortfolioRepository;

    @Autowired
    private CompanyRelationTypeRepository companyRelationTypeRepository;


    public Country someCountry(){
        return countryRepository.findOne(-1L);
    }

    public TaxOffice someTaxOffice(){
        return taxOfficeRepository.findOne(-1L);
    }

    public CompanyType someCompanyType(){
        return companyTypeRepository.findOne(-1L);
    }
    public CompanySegmentType someCompanySegmentType(){
        return companySegmentTypeRepository.findOne(-1L);
    }
    public SalesPortfolio someSalesPortfolio(){
        return salesPortfolioRepository.findOne(-1L);
    }
    public Company someCompany(){
        return companyRepository.findOne(-1L);
    }
    public Company someCompany(long id){
        return companyRepository.findOne(id);
    }
    public CompanyRelationType someCompanyRelationType(){
        return companyRelationTypeRepository.findOne(-1L);
    }
}
