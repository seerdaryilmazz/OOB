package ekol.crm.account.validator;

import ekol.crm.account.domain.model.Country;
import ekol.crm.account.repository.CountryRepository;
import ekol.exceptions.ResourceNotFoundException;
import ekol.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CountryValidator {

    private CountryRepository countryRepository;

    public void validate(Country country){

        if(country.getId() == null){
            throw new ValidationException("Country id should not be empty");
        }
        if(StringUtils.isEmpty(country.getIso())){
            throw new ValidationException("Country iso should not be empty");
        }
        if(StringUtils.isEmpty(country.getName())){
            throw new ValidationException("Country name should not be empty");
        }
        if(country.getStatus() == null){
            throw new ValidationException("Country status should not be empty");
        }

        countryRepository.findById(country.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Country with id {0} not found", country.getId())
        );

    }


}
