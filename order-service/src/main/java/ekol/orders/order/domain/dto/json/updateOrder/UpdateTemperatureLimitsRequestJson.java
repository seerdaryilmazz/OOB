package ekol.orders.order.domain.dto.json.updateOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateTemperatureLimitsRequestJson {

    private Integer minValue;
    private Integer maxValue;
}
