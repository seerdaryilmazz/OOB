package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.model.Customs;
import ekol.exceptions.ValidationException;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomsJson {

    private Long id;
    private CustomsPointJson departure;
    private CustomsPointJson arrival;

    public Customs toEntity(){
        return Customs.builder()
                .id(getId())
                .departure(getDeparture().toEntity())
                .arrival(getArrival().toEntity()).build();
    }
    public static CustomsJson fromEntity(Customs customs){
        return new CustomsJson.CustomsJsonBuilder()
                .id(customs.getId())
                .departure(CustomsPointJson.fromEntity(customs.getDeparture()))
                .arrival(CustomsPointJson.fromEntity(customs.getArrival())).build();
    }

    public void validate(){
        if(getDeparture() == null){
            throw new ValidationException("Departure customs info should not be empty");
        }
        getDeparture().validate();

        if(getArrival() == null){
            throw new ValidationException("Arrival customs info should not be empty");
        }
        getArrival().validate();
    }
}
