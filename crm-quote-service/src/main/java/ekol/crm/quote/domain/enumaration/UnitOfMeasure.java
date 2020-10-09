package ekol.crm.quote.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum UnitOfMeasure {
    KG("KG", Arrays.asList("LTL", "AIR")),
    DESI("Desi", Arrays.asList("DOMESTIC_LTL")),
    LDM("LDM", Arrays.asList("LTL", "DOMESTIC_LTL")),
    CUBIC_METER("m³", Arrays.asList("LTL", "LCL", "DOMESTIC_LTL")),
    PAY_WEIGHT("PW", Arrays.asList("LTL")),
    SHIPMENT_COUNT("Shipment Count", Arrays.asList("FTL", "LTL", "DOMESTIC_LTL", "AIR", "DEDICATED", "MILKRUN")),
    CONTAINER_COUNT("Container Count", Arrays.asList("FCL")),
    DECLARATION_COUNT("Declaration Count", Arrays.asList("CUSTOMS"));

    private String id;
    private String code;
    private String name;
    private List<String> scope;

    UnitOfMeasure(String desc, List<String> scope){
        this.id = name();
        this.code = name();
        this.name = desc;
        this.scope = scope;
    }

    @JsonCreator
    public static UnitOfMeasure fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return UnitOfMeasure.valueOf(name);
    }
}
