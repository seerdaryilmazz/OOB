package ekol.crm.account.domain.dto.potential;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.*;

import ekol.crm.account.domain.dto.*;
import ekol.crm.account.domain.enumaration.*;
import ekol.model.CodeNamePair;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.exceptions.ValidationException;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.IdNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "discriminator",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoadPotentialJson.class, name = "ROAD"),
        @JsonSubTypes.Type(value = SeaPotentialJson.class, name = "SEA"),
        @JsonSubTypes.Type(value = AirPotentialJson.class, name = "AIR"),
        @JsonSubTypes.Type(value = CustomsPotentialJson.class, name = "CCL")
})
@Getter
@Setter
@NoArgsConstructor
public abstract class PotentialJson {

    private Long id;
    private AccountJson account;
    private CountryJson fromCountry;
    private Set<CountryPointJson> fromPoint;
    private CountryJson toCountry;
    private Set<CountryPointJson> toPoint;
    private List<ShipmentLoadingType> shipmentLoadingTypes;
    private IdNamePair competitor;
    private FrequencyType frequencyType;
    private Integer frequency;
    private CodeNamePair serviceArea;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private String discriminator;
    private String createdBy;
    private UtcDateTime createdAt;
    private String lastUpdatedBy;
    private UtcDateTime lastUpdated;
    
    public PotentialStatus getStatus() {
    	LocalDate now = LocalDate.now();
    	if(now.isAfter(getValidityEndDate()) || now.isBefore(getValidityStartDate())) {
    		return PotentialStatus.INACTIVE;
    	}
    	return PotentialStatus.ACTIVE;
    }

    protected abstract void validate();

    public abstract Potential toEntity();

    public void validatePotential(){

        if(getServiceArea().getCode() == null){
            throw new ValidationException("Service area should not be empty");
        }
        if(!"CCL".equals(getServiceArea().getCode())){
            if(getFromCountry() == null){
                throw new ValidationException("From country should not be empty");
            }
            getFromCountry().validate();
            if(getToCountry() == null){
                throw new ValidationException("To country should not be empty");
            }
            getToCountry().validate();
        }
        if(getValidityStartDate() == null || getValidityEndDate() == null){
            throw new ValidationException("Validity start/end date should not be empty");
        }
        if(getFrequencyType() != null && getFrequency() == null){
            throw new ValidationException("if frequency type exists, frequency should not be empty");
        }
        validate();
    }
}

