package ekol.orders.order.domain.dto.response.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CustomsOfficeResponse {

    private Long id ;
    private String name;
    private String shortName;
}
