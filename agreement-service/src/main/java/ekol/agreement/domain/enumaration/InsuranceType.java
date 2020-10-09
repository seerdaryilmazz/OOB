package ekol.agreement.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum InsuranceType {

    INDUSTRIAL_FIRE_INSURANCE("Industrial Fire Insurance (Warehouse)"),
    MACHINE_BREAKAGE_INSURANCE("Machine Breakage Insurance"),
    THIRD_PERSON_LIABILITY_INSURANCE("3rd Person Liability Insurance"),
    TENANT_AND_NEIGHBOURHOOD_LIABILITY_INSURANCE("Tenant and Neighbourhood Liability Insurance"),
    FIRE_SUBSCRIPTION_INSURANCE("Fire Subscription Insurance (Commodity Goods)"),
    WAREHOUSE_LIABILITY_INSURANCE("Warehouse Liability Insurance");

    private String id;
    private String code;
    private String name;

    InsuranceType(String name) {
        this.id = name();
        this.code = name();
        this.name = name;
    }

    @JsonCreator
    public static InsuranceType fromNode(JsonNode node) {
        if (!node.has("id")) {
            return null;
        }
        String name = node.get("id").asText();

        return InsuranceType.valueOf(name);
    }
}
