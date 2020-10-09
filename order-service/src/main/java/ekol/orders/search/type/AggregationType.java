package ekol.orders.search.type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public enum AggregationType {
	SPECIAL("Special", null, "special", AggregationFilterType.NONE, AggregationRenderType.TOGGLEBUTTON),
	STATUS("Status", null, "status", AggregationFilterType.NONE, AggregationRenderType.CHECKBOX),
	CUSTOMER("Customer", null, "customerName", AggregationFilterType.COMPANY, AggregationRenderType.CHECKBOX),
	SENDER("Sender", "sender", "sender.companyName", AggregationFilterType.COMPANY, AggregationRenderType.CHECKBOX),
	CONSIGNEE("Consignee", "consignee", "consignee.companyName", AggregationFilterType.COMPANY, AggregationRenderType.CHECKBOX),
	SERVICE_TYPE("Service Type", null, "serviceType", AggregationFilterType.NONE, AggregationRenderType.DROPDOWNLIST),
	LOAD_TYPE("Load Type", null, "truckLoadType", AggregationFilterType.NONE, AggregationRenderType.DROPDOWNLIST),
	LOADING_COUNTRY("Loading Country", "sender", "sender.handlingLocationCountryCode", AggregationFilterType.NONE, AggregationRenderType.CHECKBOX),
	UNLOADING_COUNTRY("Unloading Country", "consignee", "consignee.handlingLocationCountryCode", AggregationFilterType.NONE, AggregationRenderType.CHECKBOX),
	;


	private final String name;
	private final String path;
	private final String field;
	private final AggregationFilterType filterType;
	private final AggregationRenderType renderType;

	public boolean isNested() {
		return !Objects.isNull(getPath());
	}

	public AbstractAggregationBuilder buildAggregation(String name, int size) {
		AbstractAggregationBuilder aggregation = AggregationBuilders.terms(name).size(size).field(getField());
		if (isNested()) {
			aggregation = AggregationBuilders.nested(name).path(getPath()).subAggregation(aggregation);
		}
		return aggregation;
	}

	public AbstractAggregationBuilder buildAggregation(int size) {
		return buildAggregation(getName(), size);
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
