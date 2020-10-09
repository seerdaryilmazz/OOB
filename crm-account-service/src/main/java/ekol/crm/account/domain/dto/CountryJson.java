package ekol.crm.account.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.enumaration.CountryStatus;
import ekol.crm.account.domain.model.Country;
import ekol.exceptions.ValidationException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class CountryJson {

    private Long id;
    private String iso;
    private String name;
    private CountryStatus status;
    private Integer rank;

    public Country toEntity(){
        return  Country.builder()
                .id(getId())
                .iso(getIso())
                .name(getName())
                .status(getStatus())
                .rank(getRank())
                .build();
    }

    public static CountryJson fromEntity(Country country){
        return new CountryJsonBuilder()
                .id(country.getId())
                .iso(country.getIso())
                .name(country.getName())
                .status(country.getStatus())
                .rank(country.getRank())
                .build();
    }

    public void validate(){

        if(getId() == null){
            throw new ValidationException("Country id should not be empty");
        }
        if(StringUtils.isEmpty(getIso())){
            throw new ValidationException("Country iso should not be empty");
        }
        if(StringUtils.isEmpty(getName())){
            throw new ValidationException("Country name should not be empty");
        }
        if(getStatus() == null){
            throw new ValidationException("Country status should not be empty");
        }
    }
}
