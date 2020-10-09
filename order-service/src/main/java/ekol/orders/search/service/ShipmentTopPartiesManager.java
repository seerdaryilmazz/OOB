package ekol.orders.search.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.InternalTopHits;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import ekol.exceptions.BadRequestException;
import ekol.orders.search.domain.HandlingPartySearchDocument;
import ekol.orders.search.domain.ShipmentPartyResult;
import ekol.orders.search.domain.ShipmentSearchDocument;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class ShipmentTopPartiesManager implements ResultsExtractor<ShipmentPartyResult>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentTopPartiesManager.class);
	
	private static final String SENDER = "sender";
	private static final String CONSIGNEE = "consignee";
	
	ElasticsearchTemplate elasticsearchTemplate;
	
	public ShipmentPartyResult query(String templateId) {
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(ShipmentSearchDocument.INDEX_NAME).withQuery(QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery("templateId", templateId)));
		queryBuilder.addAggregation(buildAggregation(SENDER));
		queryBuilder.addAggregation(buildAggregation(CONSIGNEE));
		return elasticsearchTemplate.query(queryBuilder.build(), this);
	}
	
	@Override
	public ShipmentPartyResult extract(SearchResponse response) {
		return ShipmentPartyResult.of(extractParty(response.getAggregations().get(SENDER)), extractParty(response.getAggregations().get(CONSIGNEE)));
	}
	
	private AggregationBuilder<NestedBuilder> buildAggregation(String party) {
		TopHitsBuilder topHits = AggregationBuilders
				.topHits(party)
				.addField(StringUtils.join(Arrays.asList(party, "companyId"), "."))
				.addField(StringUtils.join(Arrays.asList(party, "handlingCompanyType"), "."))
				.addField(StringUtils.join(Arrays.asList(party,"handlingLocationId"),"."))
				.addSort(SortBuilders.scoreSort().order(SortOrder.DESC))
				.setFetchSource(true)
				.setSize(1000);
		return AggregationBuilders.nested(party).path(party).subAggregation(topHits);
	}
	
	private <T> T extractAggregation(Aggregation aggregation, Class<T> termClass) {
		if (termClass.isInstance(aggregation)) {
			return termClass.cast(aggregation);
		} else {
			throw new BadRequestException("Unknown aggregation type");
		}
	}
	
	private List<HandlingPartySearchDocument> extractParty(Aggregation aggregation){
		EntityMapper mapper = new DefaultEntityMapper();

		InternalNested nested = extractAggregation(aggregation, InternalNested.class);
		InternalTopHits topHits = extractAggregation(nested.getAggregations().get(aggregation.getName()), InternalTopHits.class);

		Map<HandlingPartySearchDocument,AtomicInteger> parties = new HashMap<>();
		topHits.getHits().forEach(doc->{
			try {
				HandlingPartySearchDocument obj = mapper.mapToObject(doc.getSourceAsString(), HandlingPartySearchDocument.class);
				if(parties.containsKey(obj)) {
					parties.get(obj).incrementAndGet();
				} else {
					parties.put(obj, new AtomicInteger(0));
				}
				
			} catch(Exception e) {
				LOGGER.error(e.getMessage());
			}
		});
		return parties.entrySet().stream().sorted((o1, o2)->Integer.valueOf(o1.getValue().get()).compareTo(Integer.valueOf(o2.getValue().get()))).map(Entry::getKey).collect(Collectors.toList());
	}
	
}