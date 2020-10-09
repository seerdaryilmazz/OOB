package ekol.location.validator;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ValidationException;
import ekol.location.domain.location.customs.CustomsOffice;
import ekol.location.domain.location.customs.CustomsOfficeContact;
import ekol.location.domain.location.customs.CustomsOfficeLocation;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CustomsOfficeValidator {

    public void validateNew(CustomsOffice customsOffice){
        if (customsOffice.getId() != null) {
            throw new BadRequestException("New Customs Office should not have id");
        }
        validate(customsOffice);
    }
    public void validate(CustomsOffice customsOffice){
        if(StringUtils.isBlank(customsOffice.getName())){
            throw new ValidationException("Customs office should have a name");
        }
        if(StringUtils.isBlank(customsOffice.getLocalName())){
            throw new ValidationException("Customs office should have a local name");
        }
        if(StringUtils.isBlank(customsOffice.getShortName())){
            throw new ValidationException("Customs office should have a short name");
        }
        if(customsOffice.getCountry() == null){
            throw new ValidationException("Customs office should have a country");
        }
        if(StringUtils.isBlank(customsOffice.getCustomsCode())){
            throw new ValidationException("Customs office should have a customs code");
        }
        if(!customsOffice.getCustomsCode().startsWith(customsOffice.getCountry().getIso())){
            throw new ValidationException("Customs code should start with country code {0}", customsOffice.getCountry().getIso());
        }
        customsOffice.getLocations().forEach(this::validateLocation);
        customsOffice.getContacts().forEach(this::validateContact);
    }

    public void validateLocation(CustomsOfficeLocation location){
        if(StringUtils.isBlank(location.getName())){
            throw new ValidationException("Customs location should have a name");
        }
        if(StringUtils.isBlank(location.getLocalName())){
            throw new ValidationException("Customs location should have a local name");
        }
        if(StringUtils.isBlank(location.getPostalCode())){
            throw new ValidationException("Customs location should have a postal code");
        }
        if(location.getCountry() == null){
            throw new ValidationException("Customs location should have a country");
        }
        if(StringUtils.isBlank(location.getAddress())){
            throw new ValidationException("Customs location should have an address");
        }
    }

    public void validateContact(CustomsOfficeContact contact){
        if(StringUtils.isBlank(contact.getFirstName())){
            throw new ValidationException("Customs contact should have a first name");
        }
        if(StringUtils.isBlank(contact.getLastName())){
            throw new ValidationException("Customs contact should have a last name");
        }
    }
}
