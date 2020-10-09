package ekol.crm.account.domain.dto.potential;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.account.domain.dto.CountryPointJson;
import ekol.crm.account.domain.enumaration.*;
import ekol.crm.account.domain.model.potential.AirPotential;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.exceptions.ValidationException;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class AirPotentialJson extends PotentialJson{

    private List<ChargeableWeight> chargeableWeights;
    private String incoterm;
    private IncotermExplanation incotermExplanation;

    public Potential toEntity(){
        AirPotential entity = new AirPotential();
        entity.setId(getId());
        entity.setFromCountry(getFromCountry().toEntity());
        entity.setFromCountryPoint(Optional.ofNullable(getFromPoint()).map(Collection::stream).orElseGet(Stream::empty).map(CountryPointJson::toEntity).collect(Collectors.toSet()));
        entity.setToCountry(getToCountry().toEntity());
        entity.setToCountryPoint(Optional.ofNullable(getToPoint()).map(Collection::stream).orElseGet(Stream::empty).map(CountryPointJson::toEntity).collect(Collectors.toSet()));
        entity.setFrequencyType(getFrequencyType());
        entity.setFrequency(getFrequency());
        entity.setChargeableWeights(!CollectionUtils.isEmpty(getChargeableWeights()) ? new HashSet<>(getChargeableWeights()) : null);
        entity.setIncoterm(getIncoterm());
        entity.setIncotermExplanation(getIncotermExplanation());
        entity.setCompetitor(getCompetitor());
        entity.setValidityStartDate(getValidityStartDate());
        entity.setValidityEndDate(getValidityEndDate());
        entity.setCreatedAt(getCreatedAt());
        entity.setCreatedBy(getCreatedBy());
        entity.setServiceArea(Optional.of(getServiceArea().getCode()).orElse("AIR"));
        return entity;
    }

    public void validate(){
        if(getFromPoint() == null){
            throw new ValidationException("From airport should not be empty");
        }
        Optional.ofNullable(getFromPoint()).map(Collection::stream).orElseGet(Stream::empty).forEach(CountryPointJson::validate);
        if(getToPoint() == null){
            throw new ValidationException("To airport should not be empty");
        }
        Optional.ofNullable(getToPoint()).map(Collection::stream).orElseGet(Stream::empty).forEach(CountryPointJson::validate);
        if(CollectionUtils.isEmpty(getChargeableWeights())){
            throw new ValidationException("Chargeable weight should not be empty");
        }
        if(StringUtils.isEmpty(getIncoterm())){
            throw new ValidationException("Incoterm should not be empty");
        }
        if(getIncotermExplanation() == null){
            throw new ValidationException("Incoterm explanation should not be empty");
        }
    }
}
