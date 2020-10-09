package ekol.agreement.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum StampTaxPayer {

    EKOL("Ekol"),
    CUSTOMER("Customer"),
    EKOL_CUSTOMER("Ekol + Customer");

    private String id;
    private String name;

    StampTaxPayer(String name) {
        this.id = name();
        this.name = name;
    }

    @JsonCreator
    public static StampTaxPayer fromNode(JsonNode node) {
        if (!node.has("id")) {
            return null;
        }
        String name = node.get("id").asText();

        return StampTaxPayer.valueOf(name);
    }
}
