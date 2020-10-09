package ekol.orders.order.domain.dto.json.updateOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.json.OrderShipmentUnitJson;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateShipmentUnitsRequestJson {

    private Integer totalQuantity;
    private Set<IdNamePair> packageTypes = new HashSet<>();
    private BigDecimal grossWeight;
    private BigDecimal netWeight;
    private BigDecimal totalVolume;
    private BigDecimal totalLdm;
    private List<OrderShipmentUnitJson> units = new ArrayList<>();

}
