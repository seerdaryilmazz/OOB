package ekol.orders.search.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ekol.orders.search.type.RangeTypePreset.RangeTypePresetDefinition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
@JsonSerialize(using = RangeType.CustomSerializer.class)
public enum RangeType {
    READY_DATE("Ready Date", "readyDateOrLoadingAppointment", "readyDateOrLoadingAppointment.localDateTime", RangeFilterType.DATE, RangeTypePresetDefinition.builder().start(0).end(3).gap(1).existBeforeStart(true).build()),
    DELIVERY_APPOINTMENT_DATE("Requested Delivery Date", "deliveryDateOrUnloadingAppointment", "deliveryDateOrUnloadingAppointment.localDateTime", RangeFilterType.DATE, RangeTypePresetDefinition.builder().start(0).end(3).gap(1).existBeforeStart(true).build());

    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";

    @NonNull
    private final String name;
    @NonNull
    private final String path;
    @NonNull
    private final String field;
    @NonNull
    private final RangeFilterType filterType;
    
    @NonNull
    @Getter(AccessLevel.NONE)
    private final RangeTypePresetDefinition presetDefinition;
    
    private List<RangeTypePreset> presets = new ArrayList<>();
    
    public void refreshPresets() {
    	getPresets().clear();
    	getPresets().addAll(RangeTypePreset.generate(filterType, presetDefinition));
    }

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
            jsonGenerator.writeObjectField("presets", obj.getPresets());
            jsonGenerator.writeEndObject();
        }
    }
}
