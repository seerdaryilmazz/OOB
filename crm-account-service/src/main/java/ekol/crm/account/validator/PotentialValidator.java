package ekol.crm.account.validator;

import java.util.*;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.crm.account.domain.model.*;
import ekol.crm.account.domain.model.potential.CustomsPotential;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.crm.account.repository.AccountRepository;
import ekol.exceptions.ValidationException;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PotentialValidator {

    private AccountRepository accountRepository;

    public void validate(Potential potential){

        if(!(potential instanceof CustomsPotential)){
            if(!Optional.of(potential).map(Potential::getFromCountry).map(Country::getIso).filter(StringUtils::isNotEmpty).isPresent()){
                throw new ValidationException("From country should not be empty");
            }
            if(Optional.of(potential).map(Potential::getFromCountryPoint).map(Collection::stream).orElseGet(Stream::empty).map(CountryPoint::getId).anyMatch(Objects::isNull)){
                throw new ValidationException("From country point should not be empty");
            }
            if(!Optional.of(potential).map(Potential::getToCountry).map(Country::getIso).filter(StringUtils::isNotEmpty).isPresent()){
                throw new ValidationException("To country should not be empty");
            }
            if(Optional.of(potential).map(Potential::getToCountryPoint).map(Collection::stream).orElseGet(Stream::empty).map(CountryPoint::getId).anyMatch(Objects::isNull)){
                throw new ValidationException("To country point should not be empty");
            }
        }
        if(Objects.nonNull(potential.getFrequencyType()) && Objects.isNull(potential.getFrequency())){
            throw new ValidationException("if frequency type exists, frequency should not be empty");
        }
        if(!Optional.of(potential).map(Potential::getAccount).map(Account::getId).isPresent()){
            throw new ValidationException("Account should be assigned to a potential");
        }
        if(!accountRepository.findById(potential.getAccount().getId()).isPresent()){
            throw new ValidationException("Account with id can not found");
        }
    }


}
