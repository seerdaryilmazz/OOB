package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.enumaration.ClearanceResponsible;
import ekol.crm.quote.domain.enumaration.CustomsClearanceType;
import ekol.crm.quote.domain.model.CustomsPoint;
import ekol.exceptions.ValidationException;
import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomsPointJson {

    private ClearanceResponsible clearanceResponsible;
    private CustomsClearanceType clearanceType;
    private IdNamePair location;
    private IdNamePair office;
    private IsoNamePair customsLocationCountry;
    private IdNamePair customsLocationPostal;


    public CustomsPoint toEntity(){
        return CustomsPoint.builder()
                .clearanceResponsible(getClearanceResponsible())
                .clearanceType(getClearanceType())
                .location(getLocation())
                .office(getOffice())
                .customsLocationCountry(getCustomsLocationCountry())
                .customsLocationPostal(getCustomsLocationPostal()).build();
    }

    public static CustomsPointJson fromEntity(CustomsPoint customsPoint){
        return new CustomsPointJsonBuilder()
                .clearanceResponsible(customsPoint.getClearanceResponsible())
                .clearanceType(customsPoint.getClearanceType())
                .location(customsPoint.getLocation())
                .office(customsPoint.getOffice())
                .customsLocationCountry(customsPoint.getCustomsLocationCountry())
                .customsLocationPostal(customsPoint.getCustomsLocationPostal()).build();
    }

    public void validate(){

        if(getClearanceResponsible() == null){
            throw new ValidationException("Clearance responsible should not be empty");
        }

        if(getClearanceType() == null){
            throw new ValidationException("Customs clearance type should not be empty");
        }
    }
}

