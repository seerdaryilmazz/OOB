package ekol.agreement.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BasedOnType {
    PER_MONTH("Per Month"),
    PER_PALLET("Per Pallet"),
    PER_BOX("Per Box"),
    PER_ITEM("Per Item"),
    PER_ORDER("Per Order"),
    PER_LINE("Per Line"),
    PER_DISPATCH("Per Dispatch"),
    PER_TRUCK("Per Truck"),
    PER_PERSON("Per Person"),
    PER_M2("Per m2"),
    PER_BUNDLE("Per Bundle"),
    PER_ITS_NOTIFICATION("Per ITS Notification"),
    PER_UNIT("Per Unit"),
    PER_PALLET_MONTH("Per Pallet/month"),
    PER_PALLET_DAY("Per Pallet/day");



    private String id;
    private String code;
    private String name;

    BasedOnType(String desc){
        this.id = name();
        this.code = name();
        this.name = desc;
    }

    @JsonCreator
    public static BasedOnType fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return BasedOnType.valueOf(name);
    }
}
