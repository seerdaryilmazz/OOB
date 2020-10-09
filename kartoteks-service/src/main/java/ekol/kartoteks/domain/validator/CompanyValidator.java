package ekol.kartoteks.domain.validator;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.exceptions.BadRequestException;
import ekol.kartoteks.domain.*;
import ekol.kartoteks.repository.*;
import ekol.kartoteks.repository.common.*;
import ekol.kartoteks.service.CompanyNameGenerator;
import ekol.kartoteks.utils.LanguageStringUtils;

/**
 * Created by kilimci on 07/06/16.
 */
@Component
public class CompanyValidator {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private TaxOfficeRepository taxOfficeRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyIdMappingRepository companyIdMappingRepository;

    public void validate(Company company){
        validateCompany(company);
        validateLocations(company.getCompanyLocations());
    }

    public void validateCompany(Company company){
        company.toUpperCase();
        validateCountry(company);
        validateNames(company);
        validateTaxOfficeAndTaxId(company);
        validateEoriNumber(company);
        validateTckn(company);
        validateMapping(company);
    }

    public void validateMapping(Company company){
        List<Long> companyIds = new ArrayList<>();
        if(company.getMergeWithCompanyId() != null){
            companyIds.add(company.getMergeWithCompanyId());
        }
        companyIds.add(company.getId());
        company.getMappedIds().forEach(companyIdMapping -> {
            CompanyIdMapping existingMapping = companyIdMappingRepository.findByApplicationAndApplicationCompanyId(
                    companyIdMapping.getApplication(), companyIdMapping.getApplicationCompanyId());
            if(existingMapping != null && !companyIds.contains(existingMapping.getCompany().getId())){
                throw new BadRequestException("This company already have a mapping, if you are importing a company this could mean that this queue item has already been imported");
            }
        });
    }
    public void validateLocations(Collection<CompanyLocation> locations){
        if(locations == null || locations.isEmpty()){
            throw new BadRequestException("Company has to have at least one location");
        }
        long defaultLocationCount = locations.stream().filter(location -> location.isDefault()).collect(counting());
        if(defaultLocationCount == 0){
            throw new BadRequestException("Company has to have one location selected as default");
        }else if(defaultLocationCount > 1){
            throw new BadRequestException("Company can not have more than one location selected as default");
        }

        locations.stream().filter(t->StringUtils.isNotEmpty(t.getName()))
                .collect(groupingBy(CompanyLocation::getName, counting()))
                .entrySet().stream().filter(item -> item.getValue() > 1).findFirst().ifPresent(item -> {
                    throw new BadRequestException("Company locations must have unique names, {0} is duplicate", item.getKey());
                });

        locations.stream().filter(t->StringUtils.isNotEmpty(t.getShortName()))
                .collect(groupingBy(CompanyLocation::getShortName, counting()))
                .entrySet().stream().filter(item -> item.getValue() > 1).findFirst().ifPresent(item -> {
            throw new BadRequestException("Company locations must have unique short names, {0} is duplicate", item.getKey());
        });

    }

    public void validateSectors(Collection<CompanySector> sectors){
        if(sectors == null || sectors.isEmpty()){
            throw new BadRequestException("Company has to have at least one default sector");
        }
        long defaultSectorCount = sectors.stream().filter(sector -> sector.isDefault()).collect(counting());
        if(defaultSectorCount == 0){
            throw new BadRequestException("Company has to have one sector selected as default");
        }else if(defaultSectorCount > 1){
            throw new BadRequestException("Company can not have more than one sector selected as default");
        }
        List<Sector> sectorList = sectors.stream()
                .map(CompanySector::getSector)
                .collect(Collectors.toList());

        sectorList.stream()
                .collect(groupingBy(Sector::getCode, counting()))
                .entrySet().stream().filter(item -> item.getValue() > 1).findFirst().ifPresent(item -> {
            throw new BadRequestException("This sector already exists", item.getKey());
        });

    }

    private void validateNames(Company company){
        validateCompanyName(company);
        validateCompanyLocalName(company);
        validateCompanyShortName(company);
    }
    private void validateCompanyLocalName(Company company){
        if(StringUtils.isBlank(company.getLocalName())){
            throw new BadRequestException("Company local name can not be empty");
        }
        if(!LanguageStringUtils.checkStringForCountryAllowedChars(company.getLocalName(), company.getCountry())){
            throw new BadRequestException("Company local name has invalid characters for selected country");
        }
    }
    private void validateCompanyName(Company company){
        if(StringUtils.isBlank(company.getName())){
            throw new BadRequestException("Company name can not be empty");
        }
        if(company.getName().length() > 100){
            throw new BadRequestException("Company name can not be longer than 100 characters");
        }
        if(!LanguageStringUtils.checkStringForStandardChars(company.getName(), company.getCountry())){
            throw new BadRequestException("Company name {0} has invalid characters for selected country", company.getName());
        }
        if(company.getId() == null){
            if(companyRepository.findByName(company.getName()) !=  null){
                throw new BadRequestException("Company with name {0} already exists", company.getName());
            }
        }else{
            Company otherCompany = companyRepository.findById(company.getId());
            if(otherCompany !=  null && !otherCompany.getId().equals(company.getId()) && otherCompany.getMergeWithCompanyId() == company.getMergeWithCompanyId()){
                throw new BadRequestException("Company with name {0} already exists", company.getName());
            }

        }

    }
    private void validateCompanyShortName(Company company){
        if(StringUtils.isBlank(company.getShortName())){
            throw new BadRequestException("Company short name can not be empty");
        }
        if(company.getShortName().length() > CompanyNameGenerator.SHORT_NAME_LENGTH){
            throw new BadRequestException("Company short name can not be longer than {0} characters", CompanyNameGenerator.SHORT_NAME_LENGTH);
        }
        if(findCompanyByShortNameAndId(company.getShortName(), company.getId()) !=  null){
            throw new BadRequestException("Company with short name {0} already exists", company.getShortName());
        }
    }

    private Company findCompanyByShortNameAndId(String name, Long id){
        if(id == null){
            return companyRepository.findByShortName(name);
        }else{
            return companyRepository.findByShortNameAndIdNot(name, id);
        }
    }

    private void validateCountry(Company company){
        if(company.getCountry() == null){
            throw new BadRequestException("Company country can not be empty");
        }
        if(countryRepository.findByIsoIgnoreCase(company.getCountry().getIso()) == null){
            throw new BadRequestException("This company has a country with code {0} which does not exist",
                    company.getCountry().getIso());
        }

    }
    private void validateTaxOfficeAndTaxId(Company company){
        if(company.getTaxOffice() != null &&
                taxOfficeRepository.findByCode(company.getTaxOffice().getCode()) == null){
            throw new BadRequestException("This company has a taxoffice with code {0} which does not exist", company.getTaxOffice().getCode());
        }
        if(StringUtils.isNotBlank(company.getTaxOfficeCode()) && company.getTaxOffice() != null){
            if(!company.getTaxOffice().getCode().equals(company.getTaxOfficeCode())){
                throw new BadRequestException("This company has a tax office code incompatible with tax office");
            }
        }
        if (StringUtils.isNotBlank(company.getTaxId())) {
            Company companyThatOwnsTaxId;
            if (company.getId() == null) {
                companyThatOwnsTaxId = companyRepository.findByCountryIdAndTaxId(company.getCountry().getId(), company.getTaxId());
            } else {
                companyThatOwnsTaxId = companyRepository.findByCountryIdAndTaxIdAndIdNot(company.getCountry().getId(), company.getTaxId(), company.getId());
            }
            if (companyThatOwnsTaxId != null && companyThatOwnsTaxId.getMergeWithCompanyId() == company.getMergeWithCompanyId()) {
                throw new BadRequestException("Given tax id is used by {0}.", companyThatOwnsTaxId.getName());
            }
        }
    }
    
    private void validateEoriNumber(Company company) {
    	if(StringUtils.isNotBlank(company.getEoriNumber())) {
    		List<Company> companies = companyRepository.findByEoriNumber(company.getEoriNumber());
    		Company found = null;
    		if(Objects.isNull(company.getId())) {
    			found = companies.stream().findAny().orElse(null);
    		}else {
    			found = companies.stream().filter(t->!Objects.equals(t.getId(), company.getId())).findAny().orElse(null);
    		}
    		if(Objects.nonNull(found)) {	
    			throw new BadRequestException("Given eori number is used by {0}.", found.getName());
    		}
    	}
    }

    private void validateTckn(Company company){
        if(company.getId() == null &&
                StringUtils.isNotBlank(company.getTckn()) &&
                !companyRepository.findByTckn(company.getTckn()).isEmpty()){
            throw new BadRequestException("There is a company with TCKN {0} already exists",
                    company.getTckn());
        }
    }

    public static Set<String> findPalindromes(String input, int low, int high){
        Set<String> result = new HashSet<>();

        while (low >= 0 && high < input.length() && input.charAt(low) == input.charAt(high)) {
            System.out.println("Low: "+ input.charAt(low));
            System.out.println("High: " +input.charAt(high));
            System.out.println("Added: " +input.substring(low, high + 1));
            result.add(input.substring(low, high + 1));
            low--;
            high++;
        }
        System.out.println("Result: "+result);
        System.out.println();
        return result;
    }

    public static void main(String[] args) {
        String input = "küçük";

        List<String> palindromes = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            System.out.println("CharAt: "+ i);
            palindromes.addAll(findPalindromes(input, i, i + 1));
            palindromes.addAll(findPalindromes(input, i, i));
        }

        System.out.println(palindromes);


//        MultiThreading R1 = new MultiThreading( "Thread-1");
//        R1.start();
//
//        MultiThreading R2 = new MultiThreading( "Thread-2");
//        R2.start();

    }
}
