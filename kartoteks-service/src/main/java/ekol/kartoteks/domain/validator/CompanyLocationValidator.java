package ekol.kartoteks.domain.validator;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.exceptions.BadRequestException;
import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.domain.common.Country;
import ekol.kartoteks.repository.CompanyLocationRepository;
import ekol.kartoteks.repository.common.CountryRepository;
import ekol.kartoteks.service.CompanyNameGenerator;

/**
 * Created by kilimci on 07/06/16.
 */
@Component
public class CompanyLocationValidator {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PhoneNumberValidator phoneNumberValidator;

    @Autowired
    private CompanyLocationRepository companyLocationRepository;

    private static final Integer MAX_LENGTH_SHORT_NAME = 30;

    public void validate(CompanyLocation location){
        location.toUpperCase();
        validateName(location);
        validateShortName(location);
        validatePhoneNumbers(location);
        validatePostalAddress(location);
        validateCountry(location);
        validateCustomsCode(location);
    }

    private void validateName(CompanyLocation location){
        if(StringUtils.isBlank(location.getName())){
            throw new BadRequestException("Company location name can not be empty");
        }
        if(location.getName().length() > MAX_LENGTH_SHORT_NAME){
            throw new BadRequestException("Company location name should be shorter than 30 characters");
        }

        List<CompanyLocation> withSameNameAndDifferentId = findLocationByNameAndId(location);
        if(!withSameNameAndDifferentId.isEmpty()){
            throw new BadRequestException("Company location with name ''{0}'' already exists", location.getName());
        }
    }
    private void validateShortName(CompanyLocation location){
        if(StringUtils.isBlank(location.getShortName())){
            throw new BadRequestException("Company location short name can not be empty");
        }
        if(location.getShortName().length() > CompanyNameGenerator.LOCATION_SHORT_NAME_LENGTH){
            throw new BadRequestException("Company location ''{0}'': Company name can not be longer than {1} characters",
                    location.getShortName(), CompanyNameGenerator.LOCATION_SHORT_NAME_LENGTH);
        }
        List<CompanyLocation> withSameNameAndDifferentId = findLocationByShortNameAndId(location);
        if(!withSameNameAndDifferentId.isEmpty()){
            throw new BadRequestException("Company location with name ''{0}'' already exists", location.getName());
        }
    }
    private List<CompanyLocation> findLocationByNameAndId(CompanyLocation companyLocation){
        if(companyLocation.getId() == null){
            return companyLocationRepository.findByName(companyLocation.getName());
        }else{
            return companyLocationRepository.findByNameAndIdNot(companyLocation.getName(), companyLocation.getId());
        }
    }
    private List<CompanyLocation> findLocationByShortNameAndId(CompanyLocation companyLocation){
        if(companyLocation.getId() == null){
            return companyLocationRepository.findByShortName(companyLocation.getShortName());
        }else{
            return companyLocationRepository.findByShortNameAndIdNot(companyLocation.getShortName(), companyLocation.getId());
        }
    }

    private void validatePhoneNumbers(CompanyLocation location){
        location.getPhoneNumbers().forEach(phoneNumberValidator::validateForLocation);
    }

    private void validatePostalAddress(CompanyLocation location){
        if(StringUtils.isBlank(location.getPostaladdress().getPostalCode())){
            throw new BadRequestException("Company location ''{0}'': Postal code can not be empty", location.getName());
        }
        if(StringUtils.isBlank(location.getPostaladdress().getStreetName())){
            throw new BadRequestException("Company location ''{0}'': Street name can not be empty", location.getName());
        }
        if(location.getPostaladdress().getStreetName().length() > 120){
            throw new BadRequestException("Company location ''{0}'': Street name should not be longer than 120 characters", location.getName());
        }
        if(location.getId() != null){
            CompanyLocation existing = companyLocationRepository.findOne(location.getId());
            if(existing != null && existing.isPointOnMapConfirmed()){
                if(!existing.getPostaladdress().getPointOnMap().equals(location.getPostaladdress().getPointOnMap())){
                    throw new BadRequestException("Map location of ''{0}'' is confirmed and can not be changed", location.getName());
                }
            }
        }

    }

    private void validateCountry(CompanyLocation location){
        if(location.getPostaladdress().getCountry() == null){
            throw new BadRequestException("Company location ''{0}'': Country can not be empty", location.getName());
        }
        if(countryRepository.findByIsoIgnoreCase(location.getPostaladdress().getCountry().getIso()) == null){
            throw new BadRequestException("Company location ''{0}'': Has a country with code {1} which does not exist",
                    location.getName(), location.getPostaladdress().getCountry().getIso());
        }
    }
    private void validateCustomsCode(CompanyLocation location){
        if(StringUtils.isNotBlank(location.getCustomsCode())){
            Pattern pattern = Pattern.compile("[A-Z]{2}\\d{6}");
            Matcher matcher = pattern.matcher(location.getCustomsCode());
            if(!matcher.matches()){
                throw new BadRequestException("Customs code should be country code followed by 6 digit number, like DE123456");
            }

            String countryCode = location.getCustomsCode().substring(0,2);
            if(!location.getPostaladdress().getCountry().getIso().equals(countryCode)){
                Country customsCodeCountry = countryRepository.findByIsoIgnoreCase(countryCode);
                if(customsCodeCountry == null){
                    throw new BadRequestException("There is no country with code {0}", countryCode);
                }
                if(location.getPostaladdress().getCountry().isEuMember()){
                    if(!customsCodeCountry.isEuMember()){
                        throw new BadRequestException("Customs code should start with a EU country code");
                    }
                }else{
                    throw new BadRequestException("Customs code should start with a country code {0}", location.getPostaladdress().getCountry().getIso());
                }
            }
        }

    }
}
