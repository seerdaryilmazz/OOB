package ekol.agreement.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum RenewalDateType {
    YEARS("Year(s)"),
    MONTHS("Month(s)"),
    DAYS("Day(s)");

    private String id;
    private String name;

    RenewalDateType(String name) {
        this.id = name();
        this.name = name;
    }

    @JsonCreator
    public static RenewalDateType fromNode(JsonNode node) {
        if (!node.has("id")) {
            return null;
        }
        String name = node.get("id").asText();

        return RenewalDateType.valueOf(name);
    }
}