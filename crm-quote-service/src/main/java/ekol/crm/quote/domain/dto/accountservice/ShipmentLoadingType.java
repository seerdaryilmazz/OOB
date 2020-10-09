package ekol.crm.quote.domain.dto.accountservice;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentLoadingType implements Serializable {

    private String id;

    private String code;

    private String name;
}
