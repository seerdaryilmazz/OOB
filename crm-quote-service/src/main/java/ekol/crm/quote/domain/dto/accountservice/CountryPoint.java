package ekol.crm.quote.domain.dto.accountservice;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.enumaration.CountryPointType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryPoint implements Serializable {

    private Long id;

    private Country country;

    private CountryPointType type;

    private String code;

    private String name;

}
