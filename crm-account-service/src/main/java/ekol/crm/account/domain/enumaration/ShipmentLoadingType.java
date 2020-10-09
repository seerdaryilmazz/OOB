package ekol.crm.account.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import ekol.crm.account.domain.model.CodeNamePair;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ShipmentLoadingType {

    FTL("FTL", "23800", new BigDecimal(23800), new HashSet<>(Arrays.asList("ROAD","DTR"))),
    LTL_GROUPAGE("LTL - Groupage", "0-3500", new BigDecimal(3500), new HashSet<>(Arrays.asList("ROAD"))),
    LTL_PART_LOAD("LTL - Part Load", "3500-20000", new BigDecimal(23800), new HashSet<>(Arrays.asList("ROAD"))),

    FCL("FCL", null, null, new HashSet<>(Arrays.asList("SEA"))),
    LCL("LCL", null, null, new HashSet<>(Arrays.asList("SEA"))),

    LTL("LTL", null, null, new HashSet<>(Arrays.asList("DTR"))),
    DEDICATED("Dedicated", null, null, new HashSet<>(Arrays.asList("DTR"))),
    MILKRUN("Milkrun", null, null, new HashSet<>(Arrays.asList("DTR")));


    private String id;
    private String code;
    private String name;
    private String payWeight;
    private BigDecimal maxPayWeight;
    private Set<String> serviceAreas;


    ShipmentLoadingType(String desc, String payWeight, BigDecimal maxPayWeight, Set<String> serviceAreas){

        this.id = name();
        this.code = name();
        this.name = desc;
        this.payWeight = payWeight;
        this.maxPayWeight = maxPayWeight;
        this.serviceAreas = serviceAreas;
    }

    @JsonCreator
    public static ShipmentLoadingType fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return ShipmentLoadingType.valueOf(name);
    }
}
