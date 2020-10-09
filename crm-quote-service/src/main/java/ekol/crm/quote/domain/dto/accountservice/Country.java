package ekol.crm.quote.domain.dto.accountservice;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country implements Serializable {

    private Long id;

    private String iso;

    private String name;

}
