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
@JsonSerialize(using = SortType.CustomSerializer.class)
public enum SortType {
    READY_DATE("Ready Date", "readyDateOrLoadingAppointment.localDateTime", "readyDateOrLoadingAppointment"),
    DELIVERY_APPOINTMENT_DATE("Requested Delivery Date", "deliveryDateOrUnloadingAppointment.localDateTime", "deliveryDateOrUnloadingAppointment");

    private final String name;
    private final String field;
    private final String nestedPath;

    public SortBuilder buildSort(SortDirection sortDirection) {
        FieldSortBuilder fieldSortBuilder = SortBuilders.fieldSort(field).order(sortDirection == SortDirection.DESC ? SortOrder.DESC : SortOrder.ASC);
        if (nestedPath != null) {
            fieldSortBuilder.setNestedPath(nestedPath);
        }
        return fieldSortBuilder;
    }

    private static Map<String, SortType> MAP = new HashMap<>();

    static {
        Arrays.asList(SortType.values()).forEach(sortType -> MAP.put(sortType.getName(), sortType));
    }

    public static SortType fromName(String name) {
        return MAP.get(name);
    }

    public static enum SortDirection {
        ASC, DESC
    }

    public static class CustomSerializer extends JsonSerializer<SortType> {

        @Override
        public void serialize(
                SortType obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
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
