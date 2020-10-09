package ekol.crm.account.validator;

import ekol.crm.account.domain.model.CountryPoint;
import ekol.crm.account.repository.CountryPointRepository;
import ekol.exceptions.ResourceNotFoundException;
import ekol.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CountryPointValidator {

    private CountryPointRepository countryPointRepository;

    public void validate(CountryPoint countryPoint){

        if(countryPoint.getCountry() == null || countryPoint.getCountry().getId() == null){
            throw new ValidationException("Country should not be empty for country point");
        }
        if(countryPoint.getId() == null){
            throw new ValidationException("Country point id should not be empty");
        }
        if(countryPoint.getType() == null){
            throw new ValidationException("Country point type should not be empty");
        }
        if(StringUtils.isEmpty(countryPoint.getCode())){
            throw new ValidationException("Country point code should not be empty");
        }
        if(StringUtils.isEmpty(countryPoint.getName())){
            throw new ValidationException("Country point name should not be empty");
        }

        countryPointRepository.findById(countryPoint.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Country point with id {0} not found", countryPoint.getId())
        );

    }


}
