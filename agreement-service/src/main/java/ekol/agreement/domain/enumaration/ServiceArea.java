package ekol.agreement.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ServiceArea {
    ROAD("ROAD"),
    MARINE("MARINE"),
    AIR("AIR"),
    EXHIBITION("EXHIBITION"),
    DOMESTIC_TRANSPORT("DOMESTIC TRANSPORT"),
    CUSTOMS("CUSTOMS"),
    BOUNDED_WAREHOUSE("BOUNDED WAREHOUSE"),
    RAILWAY("RAILWAY"),
    WAREHOUSE_MANAGEMENT("WAREHOUSE MANAGEMENT"),
    INSURANCE("INSURANCE"),
    RO_RO("RO/RO");

    private String id;
    private String name;

    ServiceArea(String name) {
        this.id = name();
        this.name = name;
    }

    @JsonCreator
    public static ServiceArea fromNode(JsonNode node) {
        if (!node.has("id")) {
            return null;
        }
        String name = node.get("id").asText();

        return ServiceArea.valueOf(name);
    }
}
