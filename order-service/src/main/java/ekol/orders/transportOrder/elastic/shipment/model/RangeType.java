package ekol.orders.transportOrder.elastic.shipment.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ozer on 10/10/16.
 */
@JsonSerialize(using = RangeType.CustomSerializer.class)
public enum RangeType {
    GROSS_WEIGHT("Gross Weight", null, "grossWeight", RangeFilterType.NUMERIC),
    VOLUME("Volume", null, "volume", RangeFilterType.NUMERIC),
    LDM("LDM", null, "ldm", RangeFilterType.NUMERIC),
    READY_DATE("Ready Date", null, "readyAtDate", RangeFilterType.DATE),
    DELIVERY_APPOINTMENT_DATE("Delivery Appointment Date", "deliveryAppointment", "deliveryAppointment.start", RangeFilterType.DATE);

    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";

    private final String name;
    private final String path;
    private final String field;
    private final RangeFilterType filterType;

    private String getCorrectedGte(String gte) {
        if (getFilterType() == RangeFilterType.DATE) {
            return gte + " 00:00";
        }

        return gte;
    }

    private String getCorrectedLte(String lte) {
        if (getFilterType() == RangeFilterType.DATE) {
            return lte + " 23:59";
        }

        return lte;
    }

    RangeType(String name, String path, String field, RangeFilterType filterType) {
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

    public RangeFilterType getFilterType() {
        return filterType;
    }

    public boolean isNested() {
        return path != null;
    }

    public QueryBuilder buildQuery(String gte, String lte) {
        if (StringUtils.isBlank(gte) && StringUtils.isBlank(lte)) {
            return null;
        }

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(getField());

        if (StringUtils.isNotBlank(gte)) {
            rangeQueryBuilder.gte(getCorrectedGte(gte));
        }

        if (StringUtils.isNotBlank(lte)) {
            rangeQueryBuilder.lte(getCorrectedLte(lte));
        }

        if (getFilterType() == RangeFilterType.DATE) {
            rangeQueryBuilder.format(DATE_FORMAT);
        }

        if (isNested()) {
            return QueryBuilders.nestedQuery(getPath(), rangeQueryBuilder);
        } else {
            return rangeQueryBuilder;
        }
    }

    private static Map<String, RangeType> MAP = new HashMap<>();

    static {
        Arrays.asList(RangeType.values()).forEach(rangeType -> MAP.put(rangeType.getName(), rangeType));
    }

    public static RangeType fromName(String name) {
        return MAP.get(name);
    }

    public static class CustomSerializer extends JsonSerializer<RangeType> {

        @Override
        public void serialize(
                RangeType obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
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
