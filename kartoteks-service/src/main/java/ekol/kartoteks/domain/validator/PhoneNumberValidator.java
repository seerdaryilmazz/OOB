package ekol.kartoteks.domain.validator;

import ekol.exceptions.BadRequestException;
import ekol.kartoteks.domain.PhoneNumberWithType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 07/06/16.
 */
@Component
public class PhoneNumberValidator {

    private static final int PHONE_LIMIT = 20;
    private static final int REGION_LIMIT = 5;
    private static final int COUNTRY_LIMIT = 5;
    private static final int EXTENSION_LIMIT = 6;

    public void validateForLocation(PhoneNumberWithType phoneNumberWithType){
        if(StringUtils.isBlank(phoneNumberWithType.getPhoneNumber().getCountryCode())){
            throw new BadRequestException("Phone number must have a country code");
        }
        
        if(phoneNumberWithType.getPhoneCountryCode().length() > COUNTRY_LIMIT){
            throw new BadRequestException("Phone number country code can not be longer than {0} digits", COUNTRY_LIMIT);
        }
        if(!StringUtils.equals(phoneNumberWithType.getPhoneNumber().getCountryCode(), "34") && StringUtils.isBlank(phoneNumberWithType.getPhoneNumber().getRegionCode())){
            throw new BadRequestException("Phone number must have a region code");
        }
        if(phoneNumberWithType.getPhoneRegionCode().length() > REGION_LIMIT){
            throw new BadRequestException("Phone number region code can not be longer than {0} digits", REGION_LIMIT);
        }
        if(StringUtils.isBlank(phoneNumberWithType.getPhoneNumber().getPhone())){
            throw new BadRequestException("Phone number can not be empty");
        }
        if(phoneNumberWithType.getPhoneNumber().getPhone().length() > PHONE_LIMIT){
            throw new BadRequestException("Phone number can not be longer than {0} digits", PHONE_LIMIT);
        }
    }

    public void validateForContact(PhoneNumberWithType phoneNumberWithType){
        if(StringUtils.isBlank(phoneNumberWithType.getPhoneNumber().getPhone())){
            throw new BadRequestException("Phone number can not be empty");
        }
        if(phoneNumberWithType.getUsageType() == null){
            throw new BadRequestException("Phone number usage must be chosen");
        }
        if(phoneNumberWithType.getPhoneNumber().getPhone().length() > PHONE_LIMIT){
            throw new BadRequestException("Phone number can not be longer than {0} digits", PHONE_LIMIT);
        }
        if(phoneNumberWithType.getPhoneRegionCode().length() > REGION_LIMIT){
            throw new BadRequestException("Phone number region code can not be longer than {0} digits", REGION_LIMIT);
        }
        if(phoneNumberWithType.getPhoneCountryCode().length() > COUNTRY_LIMIT){
            throw new BadRequestException("Phone number country code can not be longer than {0} digits", COUNTRY_LIMIT);
        }
        if(StringUtils.isNotBlank(phoneNumberWithType.getPhoneNumber().getExtension()) &&
                phoneNumberWithType.getPhoneNumber().getExtension().length() > EXTENSION_LIMIT){
            throw new BadRequestException("Phone number extension code can not be longer than {0} digits", EXTENSION_LIMIT);
        }
    }
}
