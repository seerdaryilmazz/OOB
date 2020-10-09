package ekol.orders.transportOrder.elastic.shipment.model;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ozer on 10/10/16.
 */
public enum AggregationType {
    STATUS("Status", null, "status", AggregationFilterType.NONE),
    LOADING_COUNTRY("Loading Country", "sender.location", "sender.location.countryIsoCode", AggregationFilterType.COUNTRY),
    UNLOADING_COUNTRY("Unloading Country", "receiver.location", "receiver.location.countryIsoCode", AggregationFilterType.COUNTRY),
    CUSTOMER("Customer", null, "customerName", AggregationFilterType.COMPANY),
    SENDER("Sender", "sender", "sender.companyName", AggregationFilterType.COMPANY),
    RECEIVER("Consignee", "receiver", "receiver.companyName", AggregationFilterType.COMPANY);


    private final String name;
    private final String path;
    private final String field;
    private final AggregationFilterType filterType;

    AggregationType(String name, String path, String field, AggregationFilterType filterType) {
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

    public AggregationFilterType getFilterType() {
        return filterType;
    }

    public boolean isNested() {
        return path != null;
    }

    public AbstractAggregationBuilder buildAggregation(String name, int size) {
        AbstractAggregationBuilder aggregation = AggregationBuilders.terms(name).size(size).field(getField());
        if (isNested()) {
            aggregation = AggregationBuilders.nested(name).path(getPath()).subAggregation(aggregation);
        }
        return aggregation;
    }

    public AbstractAggregationBuilder buildAggregation(int size) {
        return this.buildAggregation(getName(), size);
    }

    public QueryBuilder buildQuery(String value) {
        QueryBuilder queryBuilder = QueryBuilders.matchQuery(getField(), value);
        if (isNested()) {
            queryBuilder = QueryBuilders.nestedQuery(getPath(), queryBuilder);
        }
        return queryBuilder;
    }

    private static Map<String, AggregationType> MAP = new HashMap<>();

    static {
        Arrays.asList(AggregationType.values()).forEach(aggregationType -> MAP.put(aggregationType.getName(), aggregationType));
    }

    public static AggregationType fromName(String name) {
        return MAP.get(name);
    }
}
