package ekol.crm.quote.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum CalculationType {
    MONTHLY_RENT_KM_INCLUDED("Monthly rent + km included", "DEDICATED"),
    MONTHLY_RENT_KM_UNIT("Monthly rent + km unit", "DEDICATED"),
    FTL("FTL", "FTL"),
    MILKRUN("Milkrun", "MILKRUN"),
    CUMULATIVE("Cumulative", "LTL"),
    PER_PART("Per part", "LTL"),
    WITH_SCALE("With scale", "LTL");

    private String id;
    private String code;
    private String name;
    private String shipmentLoadingType;


    CalculationType(String desc, String shipmentLoadingType){
        this.id = name();
        this.code = name();
        this.name = desc;
        this.shipmentLoadingType = shipmentLoadingType;
    }

    @JsonCreator
    public static CalculationType fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return CalculationType.valueOf(name);
    }
}
