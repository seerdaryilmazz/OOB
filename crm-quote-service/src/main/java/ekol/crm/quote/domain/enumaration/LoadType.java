package ekol.crm.quote.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum LoadType {
    ADR("Dangerous/ADR Goods", "riskFactor", "ADR", "ROAD"),
    FRIGO("Frigo Goods", "temperature", "FRIGO", "ROAD"),
    LONG("Long Goods", null, "LONG", "ROAD"),
    ROAD_OVERSIZE("Oversize Goods", null, "OVERSIZE", "ROAD"),
    FRAGILE("Fragile/Valuable Goods", null, "FRAGILE", "ROAD"),
    FOOD("Food Product", null, null, "ROAD"),

    SEA_IMO("Dangerous Goods(IMO)", null, "SEA_IMO", "SEA"),
    ISOTANK("Isotank", null, null, "SEA"),
    FLEXITANK("Flexitank", null, null, "SEA"),
    SEA_OVERSIZE("Oversize Goods", null, null, "SEA"),

    AIR_IMO("Dangerous Goods(IMO)", null, "AIR_IMO", "AIR");

    private String id;
    private String code;
    private String name;
    private String feature;
    private String billingItemName;
    private String serviceArea;


    LoadType(String desc, String feature, String billingItemName, String serviceArea){
        this.id = name();
        this.code = name();
        this.name = desc;
        this.billingItemName = billingItemName;
        this.feature = feature;
        this.serviceArea = serviceArea;
    }

    @JsonCreator
    public static LoadType fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return LoadType.valueOf(name);
    }
}