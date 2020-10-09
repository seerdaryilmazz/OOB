package ekol.orders.transportOrder.elastic.shipment.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ozer on 10/10/16.
 */
@JsonSerialize(using = GroupType.CustomSerializer.class)
public enum GroupType {
    CUSTOMER_NAME("Customer Name", "customerName"),
    SENDER_POSTAL_CODE("Sender Postal Code", "senderPostalCode"),
    RECEIVER_POSTAL_CODE("Consignee Postal Code", "receiverPostalCode"),
    SENDER_COMPANY_NAME("Sender Company", "senderCompanyName"),
    RECEIVER_COMPANY_NAME("Consignee Company", "receiverCompanyName"),
    TRANSFER_WAREHOUSE_NAME("Transfer Warehouse", "transferWarehouseName");

    public static final String GROUP_AGGREGATION_NAME = "GRP";

    private final String name;
    private final String field;

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }

    GroupType(String name, String field) {
        this.name = name;
        this.field = field;
    }

    public AbstractAggregationBuilder buildAggregation(List<SortBuilder> sorts, int size) {
        TopHitsBuilder topHitsBuilder = AggregationBuilders.topHits(GROUP_AGGREGATION_NAME).setSize(size).setFrom(0);
        if (sorts != null && !sorts.isEmpty()) {
            sorts.forEach(sort -> {
                topHitsBuilder.addSort(sort);
            });
        }

        return AggregationBuilders.terms(GROUP_AGGREGATION_NAME).field(getField()).subAggregation(topHitsBuilder);
    }

    private static Map<String, GroupType> MAP = new HashMap<>();

    static {
        Arrays.asList(GroupType.values()).forEach(sortType -> MAP.put(sortType.getName(), sortType));
    }

    public static GroupType fromName(String name) {
        return MAP.get(name);
    }

    public static class CustomSerializer extends JsonSerializer<GroupType> {

        @Override
        public void serialize(
                GroupType obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("name");
            jsonGenerator.writeString(obj.getName());
            jsonGenerator.writeFieldName("field");
            jsonGenerator.writeString(obj.getField());
            jsonGenerator.writeEndObject();
        }
    }
}
