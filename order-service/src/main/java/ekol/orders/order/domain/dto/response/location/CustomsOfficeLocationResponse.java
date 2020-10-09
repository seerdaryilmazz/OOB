package ekol.orders.order.domain.dto.response.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.orders.order.domain.dto.response.kartoteks.Country;
import ekol.orders.order.domain.dto.response.kartoteks.Point;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class CustomsOfficeLocationResponse {
	private Long id ;
    private String name;
    private String timezone;
    private String postalCode;
    private Country country;
    private Point pointOnMap;
}
