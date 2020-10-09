package ekol.crm.account.domain.enumaration;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ChargeableVolume {
    FEET_20("20'", "20'", ShipmentLoadingType.FCL, BigDecimal.ONE),
    FEET_40("40'", "40'", ShipmentLoadingType.FCL, new BigDecimal("2")),
    FEET_45("45'", "45'", ShipmentLoadingType.FCL, new BigDecimal("2")),

    CUBIC_METER_0_5("0-5", "0-5", ShipmentLoadingType.LCL, null),
    CUBIC_METER_5_10("5-10", "5-10", ShipmentLoadingType.LCL, null),
    CUBIC_METER_10_15("10-15", "10-15", ShipmentLoadingType.LCL, null),
    CUBIC_METER_15_PLUS("15+", "15+", ShipmentLoadingType.LCL, null);

    private String id;
    private String code;
    private String name;
    private ShipmentLoadingType shipmentLoadingType;
    private BigDecimal teuUnit;

    ChargeableVolume(String desc, String code, ShipmentLoadingType shipmentLoadingType, BigDecimal teuUnit){
        this.id = name();
        this.code = code;
        this.name = desc;
        this.shipmentLoadingType = shipmentLoadingType;
        this.teuUnit = teuUnit;
    }

    @JsonCreator
    public static ChargeableVolume fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return ChargeableVolume.valueOf(name);
    }
}
