package ekol.orders.transportOrder.elastic.shipment.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ozer on 13/01/17.
 */
@JsonSerialize(using = MatchType.CustomSerializer.class)
public enum MatchType {
    SHIPMENT_CODE("Shipment Code", null, "code", MatchFilterType.TEXT);

    private final String name;
    private final String path;
    private final String field;
    private final MatchFilterType filterType;

    MatchType(String name, String path, String field, MatchFilterType filterType) {
        this.name = name;
        this.path = path;
        this.field = field;
        this.filterType = filterType;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getField() {
        return field;
    }

    public MatchFilterType getFilterType() {
        return filterType;
    }

    public boolean isNested() {
        return path != null;
    }

    public QueryBuilder buildQuery(String val) {
        if (StringUtils.isBlank(val)) {
            return null;
        }

        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(getField(), val);

        if (isNested()) {
            return QueryBuilders.nestedQuery(getPath(), matchQueryBuilder);
        } else {
            return matchQueryBuilder;
        }
    }

    private static Map<String, MatchType> MAP = new HashMap<>();

    static {
        Arrays.asList(MatchType.values()).forEach(rangeType -> MAP.put(rangeType.getName(), rangeType));
    }

    public static MatchType fromName(String name) {
        return MAP.get(name);
    }

    public static class CustomSerializer extends JsonSerializer<MatchType> {

        @Override
        public void serialize(
                MatchType obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("name");
            jsonGenerator.writeString(obj.getName());
            jsonGenerator.writeFieldName("path");
            jsonGenerator.writeString(obj.getPath());
            jsonGenerator.writeFieldName("field");
            jsonGenerator.writeString(obj.getField());
            jsonGenerator.writeFieldName("filterType");
            jsonGenerator.writeString(obj.getFilterType().name());
            jsonGenerator.writeEndObject();
        }
    }
}
