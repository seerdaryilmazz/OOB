package ekol.crm.account.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.enumaration.CountryPointType;
import ekol.crm.account.domain.model.CountryPoint;
import ekol.exceptions.ValidationException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class CountryPointJson {

    private Long id;
    private CountryJson country;
    private CountryPointType type;
    private String code;
    private String name;

    public CountryPoint toEntity(){
        return  CountryPoint.builder()
                .id(getId())
                .country(getCountry().toEntity())
                .type(getType())
                .code(getCode())
                .name(getName())
                .build();
    }

    public static CountryPointJson fromEntity(CountryPoint point){
        return new CountryPointJson.CountryPointJsonBuilder()
                .id(point.getId())
                .country(CountryJson.fromEntity(point.getCountry()))
                .type(point.getType())
                .code(point.getCode())
                .name(point.getName())
                .build();
    }

    public void validate(){
        if(getCountry() == null || getCountry().getId() == null){
            throw new ValidationException("Country should not be empty for country point");
        }
        if(getId() == null){
            throw new ValidationException("Country point id should not be empty");
        }
        if(getType() == null){
            throw new ValidationException("Country point type should not be empty");
        }
        if(StringUtils.isEmpty(getCode())){
            throw new ValidationException("Country point code should not be empty");
        }
        if(StringUtils.isEmpty(getName())){
            throw new ValidationException("Country point name should not be empty");
        }
    }
}
