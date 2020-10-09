package ekol.crm.quote.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum LoadingType {
    CUSTOMER_ADDRESS("Customer Address", null, null, new String[]{"ROAD", "DTR", "SEA", "AIR"}),
    EKOL_CROSS_DOCK("Ekol Cross Dock", "EKOL_WAREHOUSE", "WAREHOUSE", new String[]{"ROAD", "DTR"}),
    PARTNER_CROSS_DOCK("Partner Cross Dock", "PARTNER_WAREHOUSE", "WAREHOUSE", new String[]{"ROAD", "DTR"}),
	PORT_ADDRESS("Port Address", null, null, new String[] {"SEA", "AIR"}),
	;

    private String id;
    private String code;
    private String name;
    private String ownerType;
    private String source;
    private String[] serviceArea;

    LoadingType(String desc,
                String ownerType,
                String source,
                String[] serviceArea){
        this.id = name();
        this.code = name();
        this.name = desc;
        this.ownerType = ownerType;
        this.source = source;
        this.serviceArea = serviceArea;
    }

    @JsonCreator
    public static LoadingType fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return LoadingType.valueOf(name);
    }
}
