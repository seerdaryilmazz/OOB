package ekol.crm.quote.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum DeliveryType {
    CUSTOMER_ADDRESS("Customer Address", null, null, null, new String[]{"ROAD", "DTR", "SEA", "AIR"}),
    EKOL_CROSS_DOCK("Ekol Cross Dock", "EKOL_WAREHOUSE", new String[] {"EXPORT", "NON_TURKEY", ""}, "WAREHOUSE", new String[]{"ROAD", "DTR"}),
    PARTNER_CROSS_DOCK("Partner Cross Dock", "PARTNER_WAREHOUSE", new String[] {"EXPORT", "NON_TURKEY", ""}, "WAREHOUSE", new String[]{"ROAD", "DTR"}),
    CUSTOMS_ADDRESS("Customs Address", null, new String[] {"IMPORT", "EXPORT", "NON_TURKEY"}, null, new String[]{"ROAD", "DTR"}),
    PORT_ADDRESS("Port Address", null,  null, null, new String[] {"SEA", "AIR"});

    private String id;
    private String code;
    private String name;
    private String[] operation;
    private String ownerType;
    private String source;
    private String[] serviceArea;

    DeliveryType(String desc,
                 String ownerType,
                 String[] operation,
                 String source,
                 String[] serviceArea){
        this.id = name();
        this.code = name();
        this.name = desc;
        this.ownerType = ownerType;
        this.operation = operation;
        this.source = source;
        this.serviceArea = serviceArea;
    }

    @JsonCreator
    public static DeliveryType fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return DeliveryType.valueOf(name);
    }
}