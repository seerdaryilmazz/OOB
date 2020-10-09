package ekol.crm.quote.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum RoundType {
    SINGLE("Single", null),
    MULTIPLE("Multiple", Arrays.asList(1,2,3,4,5));

    private String id;
    private String code;
    private String name;
    private List<Integer> rounds;


    RoundType(String desc, List<Integer> rounds){
        this.id = name();
        this.code = name();
        this.name = desc;
        this.rounds = rounds;
    }

    @JsonCreator
    public static RoundType fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return RoundType.valueOf(name);
    }
}
