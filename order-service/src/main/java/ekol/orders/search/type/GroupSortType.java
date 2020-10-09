package ekol.orders.search.type;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access=AccessLevel.PRIVATE)
@JsonSerialize(using = GroupSortType.CustomSerializer.class)
public enum GroupSortType {
    CUSTOMER("Customer", "customerName", null),
    SENDER("Sender", "sender.companyName", "sender"),
    CONSIGNEE("Consignee", "consignee.companyName", "consignee"),
    LOADING_COUNTRY("Loading Country", "sender.handlingLocationCountryCode", "sender"),
    UNLOADING_COUNTRY("Unloading Country", "consignee.handlingLocationCountryCode", "consignee"),
    ;
    private final String name;
    private final String field;
    private final String nestedPath;

    public SortBuilder buildSort(SortDirection sortDirection) {
        FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort(field).order(SortOrder.ASC);
        if (nestedPath != null) {
            fieldSortBuilder.setNestedPath(nestedPath);
        }
        return fieldSortBuilder;
    }

    private static Map<String, GroupSortType> MAP = new HashMap<>();

    static {
        Arrays.asList(GroupSortType.values()).forEach(sortType -> MAP.put(sortType.getName(), sortType));
    }

    public static GroupSortType fromName(String name) {
        return MAP.get(name);
    }

    public static enum SortDirection {
        ASC, DESC
    }

    public static class CustomSerializer extends JsonSerializer<GroupSortType> {

        @Override
        public void serialize(
                GroupSortType obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("name");
            jsonGenerator.writeString(obj.getName());
            jsonGenerator.writeFieldName("field");
            jsonGenerator.writeString(obj.getField());
            jsonGenerator.writeFieldName("nestedPath");
            jsonGenerator.writeString(obj.getNestedPath());
            jsonGenerator.writeEndObject();
        }
    }
}
