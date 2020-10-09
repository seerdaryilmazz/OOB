package ekol.crm.account.domain.dto.potential;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.account.domain.dto.CountryPointJson;
import ekol.crm.account.domain.enumaration.*;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.crm.account.domain.model.potential.SeaPotential;
import ekol.exceptions.ValidationException;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class SeaPotentialJson extends PotentialJson{

    private List<ChargeableVolume> chargeableVolumes;
    private List<ContainerType> containerTypes;
    private String incoterm;
    private IncotermExplanation incotermExplanation;

    public Potential toEntity(){
        SeaPotential entity = new SeaPotential();
        entity.setId(getId());
        entity.setFromCountry(getFromCountry().toEntity());
        entity.setFromCountryPoint(Optional.ofNullable(getFromPoint()).map(Collection::stream).orElseGet(Stream::empty).map(CountryPointJson::toEntity).collect(Collectors.toSet()));
        entity.setToCountry(getToCountry().toEntity());
        entity.setToCountryPoint(Optional.ofNullable(getToPoint()).map(Collection::stream).orElseGet(Stream::empty).map(CountryPointJson::toEntity).collect(Collectors.toSet()));
        entity.setShipmentLoadingTypes(!CollectionUtils.isEmpty(getShipmentLoadingTypes()) ? new HashSet<>(getShipmentLoadingTypes()) : null);
        entity.setFrequencyType(getFrequencyType());
        entity.setFrequency(getFrequency());
        entity.setChargeableVolumes(!CollectionUtils.isEmpty(getChargeableVolumes()) ? new HashSet<>(getChargeableVolumes()) : null);
        entity.setContainerTypes(!CollectionUtils.isEmpty(getContainerTypes()) ? new HashSet<>(getContainerTypes()) : null);
        entity.setIncoterm(getIncoterm());
        entity.setIncotermExplanation(getIncotermExplanation());
        entity.setCompetitor(getCompetitor());
        entity.setValidityStartDate(getValidityStartDate());
        entity.setValidityEndDate(getValidityEndDate());
        entity.setCreatedAt(getCreatedAt());
        entity.setCreatedBy(getCreatedBy());
        entity.setServiceArea(Optional.of(getServiceArea().getCode()).orElse("SEA"));
        return entity;
    }

    public void validate(){
        if(getFromPoint() == null){
            throw new ValidationException("From port should not be empty");
        }
        Optional.ofNullable(getFromPoint()).map(Collection::stream).orElseGet(Stream::empty).forEach(CountryPointJson::validate);
        if(getToPoint() == null){
            throw new ValidationException("To port should not be empty");
        }
        Optional.ofNullable(getToPoint()).map(Collection::stream).orElseGet(Stream::empty).forEach(CountryPointJson::validate);
        if(CollectionUtils.isEmpty(getShipmentLoadingTypes())){
            throw new ValidationException("Potential should have shipment loading type info");
        }
        if(CollectionUtils.isEmpty(getChargeableVolumes())){
            throw new ValidationException("Chargeable volume should not be empty");
        }
        if(getShipmentLoadingTypes().get(0) == ShipmentLoadingType.FCL && CollectionUtils.isEmpty(getContainerTypes())){
            throw new ValidationException("Container type should not be empty");
        }
        if(StringUtils.isEmpty(getIncoterm())){
            throw new ValidationException("Incoterm should not be empty");
        }
        if(getIncotermExplanation() == null){
            throw new ValidationException("Incoterm explanation should not be empty");
        }
    }
}
