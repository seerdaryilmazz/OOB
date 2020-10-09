package ekol.orders.order.domain.dto.json.updateOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.orders.order.domain.Status;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateOrderStatusRequestJson {
    private Status status;
}
