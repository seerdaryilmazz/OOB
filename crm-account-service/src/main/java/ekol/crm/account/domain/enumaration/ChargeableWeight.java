package ekol.crm.account.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ChargeableWeight {
    _0_45("0-45", "0-45"),
    _45_100("45-100", "45-100"),
    _100_300("100-300", "100-300"),
    _300_500("300-500", "300-500"),
    _500_750("500-750", "500-750"),
    _750_1000("750-1000", "750-1000"),
    _1000_PLUS("1000+", "1000+"),
    CHARTER("Charter", "CHARTER");

    private String id;
    private String code;
    private String name;

    ChargeableWeight(String desc, String code){
        this.id = name();
        this.code = code;
        this.name = desc;
    }

    @JsonCreator
    public static ChargeableWeight fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return ChargeableWeight.valueOf(name);
    }
}
