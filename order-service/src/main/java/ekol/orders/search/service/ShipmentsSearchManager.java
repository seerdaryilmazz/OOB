package ekol.orders.search.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.InternalSingleBucketAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.tophits.InternalTopHits;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import ekol.exceptions.BadRequestException;
import ekol.orders.search.config.AggregationFilter;
import ekol.orders.search.config.Bucket;
import ekol.orders.search.config.BucketItem;
import ekol.orders.search.config.RangeFilter;
import ekol.orders.search.config.ShipmentsSearchConfig;
import ekol.orders.search.domain.ShipmentSearchDocument;
import ekol.orders.search.domain.ShipmentsSearchResult;
import ekol.orders.search.type.AggregationType;
import ekol.orders.search.type.GroupSortType;
import ekol.orders.search.type.MatchType;
import ekol.orders.search.type.RangeType;
import ekol.orders.search.type.RangeTypePreset;
import ekol.orders.search.type.SortType;
import ekol.orders.transportOrder.elastic.shipment.model.GroupType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor=@__(@Autowired))
public class ShipmentsSearchManager implements ResultsExtractor<ShipmentsSearchResult> {
	private static final String GLOBAL_AGGREGATION_PREFIX = "All ";

	@NonNull
	private ElasticsearchTemplate elasticsearchTemplate;

	private ShipmentsSearchConfig config;
	private NativeSearchQueryBuilder queryBuilder;
	private SearchQuery searchQuery;

	public ShipmentsSearchManager setConfig(ShipmentsSearchConfig config) {
		this.config = config;
		return this;
	}


	private void init() {
		queryBuilder = new NativeSearchQueryBuilder().withIndices(ShipmentSearchDocument.INDEX_NAME);

		if (StringUtils.isBlank(config.getGroup())) {
			queryBuilder.withPageable(new PageRequest(config.getPage() - 1, config.getSize()));
		} else {
			// We won't use search hits
			queryBuilder.withPageable(new PageRequest(0, 1));
		}
	}

	private void addAggregations() {
		if(null == config) {
			return;
		}
		for (AggregationType aggregationType : AggregationType.values()) {
			int bucketSize = getBucketSizeForAggregation(aggregationType.getName());
			queryBuilder.addAggregation(aggregationType.buildAggregation(bucketSize + 1));

			if (config.getAggregationFilters() != null
					&& !config.getAggregationFilters().isEmpty()
					&& config.findAggregationFilterForAggregationType(aggregationType.getName()) != null) {
				String globalAggregationName = getGlobalAggregationName(aggregationType.getName());
				queryBuilder.addAggregation(
						AggregationBuilders.global(globalAggregationName)
						.subAggregation(AggregationBuilders.filter(globalAggregationName)
								.filter(getFilters(aggregationType.getName()))
								.subAggregation(aggregationType.buildAggregation(globalAggregationName, bucketSize + 1))));
			}
		}

		if (StringUtils.isNotBlank(config.getGroup())) {
			queryBuilder.addAggregation(GroupType.fromName(config.getGroup()).buildAggregation(getSorts(), config.getGroupBucketSize()));
		}
	}

	private BoolQueryBuilder getFilters(String exclude) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (null != config.getAggregationFilters()) {
			config.getAggregationFilters().forEach(aggregationFilter -> {
				if (aggregationFilter.getBucketName().equals(exclude)) {
					return;
				}

				AggregationType aggregationType = AggregationType.fromName(aggregationFilter.getBucketName());
				BoolQueryBuilder bucketQuery = new BoolQueryBuilder();
				aggregationFilter.getValues().forEach(value -> bucketQuery.should(aggregationType.buildQuery(value)));
				boolQueryBuilder.must(bucketQuery);
			});
		}

		if (config.getRangeFilters() != null) {
			config.getRangeFilters().forEach(rangeFilter -> {
				RangeType rangeType = RangeType.fromName(rangeFilter.getName());
				QueryBuilder rangeQueryBuilder = rangeType.buildQuery(rangeFilter.getGte(), rangeFilter.getLte());
				if (rangeQueryBuilder != null) {
					boolQueryBuilder.must(rangeQueryBuilder);
				}
			});
		}

		if (config.getMatchFilters() != null) {
			config.getMatchFilters().forEach(matchFilter -> {
				MatchType matchType = MatchType.fromName(matchFilter.getName());
				QueryBuilder matchQueryBuilder = matchType.buildQuery(matchFilter.getVal());
				if (matchQueryBuilder != null) {
					boolQueryBuilder.must(matchQueryBuilder);
				}
			});
		}

		if (!boolQueryBuilder.hasClauses()) {
			boolQueryBuilder.should(QueryBuilders.matchAllQuery());
		}

		return boolQueryBuilder;
	}

	private List<SortBuilder> getSorts() {
		if (config.getSorts() != null && !config.getSorts().isEmpty()) {
			List<SortBuilder> sorts = new ArrayList<>();
			config.getSorts().forEach(sort -> {
				SortType sortType = SortType.fromName(sort.getName());
				if (sortType != null) {
					sorts.add(sortType.buildSort("DESC".equalsIgnoreCase(sort.getDirection()) ? SortType.SortDirection.DESC : SortType.SortDirection.ASC));
				}
			});

			return sorts;
		}

		return null;
	}

	private void addSorts() {
		if(null == config) {
			return;
		} else if(CollectionUtils.isEmpty(config.getSorts())) {
			queryBuilder.withSort(SortBuilders.fieldSort("orderCode").order(SortOrder.DESC));
			return;
		}
		List<SortBuilder> sorts = getSorts();
		if (sorts != null && !sorts.isEmpty()) {
			sorts.forEach(sort -> {
				queryBuilder.withSort(sort);
			});
		}
	}
	
	private List<SortBuilder> getGroupSorts() {
		if (!CollectionUtils.isEmpty(config.getGroupSorts())) {
			List<SortBuilder> sorts = new ArrayList<>();
			config.getGroupSorts().forEach(sort -> {
				GroupSortType sortType = GroupSortType.fromName(sort.getName());
				if (sortType != null) {
					sorts.add(sortType.buildSort("DESC".equalsIgnoreCase(sort.getDirection()) ? GroupSortType.SortDirection.DESC : GroupSortType.SortDirection.ASC));
				}
			});
			
			return sorts;
		}
		
		return null;
	}
	
	private void addGroupSorts() {
		if(null == config) {
			return;
		}
		List<SortBuilder> sorts = getGroupSorts();
		if (sorts != null && !sorts.isEmpty()) {
			sorts.forEach(sort -> {
				queryBuilder.withSort(sort);
			});
		}
	}

	private ShipmentsSearchResult query(String q) {
		if(StringUtils.isNotEmpty(q)) {
			queryBuilder.withQuery(QueryBuilders.queryStringQuery(q));
		}
		searchQuery = queryBuilder.withQuery(getFilters(null)).build();
		return elasticsearchTemplate.query(searchQuery, this);
	}

	private <T> T extractAggregation(Aggregation aggregation, Class<T> termClass) {
		if (termClass.isInstance(aggregation)) {
			return termClass.cast(aggregation);
		} else if (aggregation instanceof InternalSingleBucketAggregation) {
			InternalSingleBucketAggregation complexAggregation = (InternalSingleBucketAggregation) aggregation;
			return extractAggregation(complexAggregation.getAggregations().asList().get(0), termClass);
		} else {
			throw new BadRequestException("Unknown aggregation type");
		}
	}

	private boolean isInList(List<BucketItem> items, String key) {
		List<BucketItem> filteredItems = items.stream().filter(item -> item.getKey().equals(key)).collect(Collectors.toList());
		return filteredItems != null && !filteredItems.isEmpty();
	}

	private String getGlobalAggregationName(String name) {
		return GLOBAL_AGGREGATION_PREFIX + name;
	}

	private int getBucketSizeForAggregation(String name) {
		return config.getBucketSizes() != null && config.getBucketSizes().containsKey(name) ? config.getBucketSizes().get(name) : config.getAggregationBucketSize();
	}

	private List<Bucket> flattenAggregations(Map<String, StringTerms> aggregationMap) {
		List<Bucket> buckets = new ArrayList<>();

		aggregationMap.entrySet().forEach(entry -> {
			if (entry.getKey().startsWith(GLOBAL_AGGREGATION_PREFIX) || entry.getKey().equalsIgnoreCase(GroupType.GROUP_AGGREGATION_NAME)) {
				// Special case for global and top_hits aggregations
				return;
			}

			List<BucketItem> items = new ArrayList<>();

			AggregationFilter aggregationFilter = config.findAggregationFilterForAggregationType(entry.getKey());
			AggregationType aggregationType = AggregationType.fromName(entry.getKey());

			// Add default aggregations as selected
			entry.getValue().getBuckets().forEach(b -> items.add(new BucketItem(b.getKey(), b.getDocCount(), aggregationFilter != null && aggregationFilter.getValues().contains(b.getKey()))));

			// Add not found aggregations as count 0
			if (aggregationFilter != null && !aggregationFilter.getValues().isEmpty()) {
				aggregationFilter.getValues().forEach(value -> {
					if (!isInList(items, value)) {
						items.add(new BucketItem(value, 0, true));
					}
				});
			}

			// Unselected global
			StringTerms globalAggregation = aggregationMap.get(getGlobalAggregationName(entry.getKey()));
			if (globalAggregation != null) {
				globalAggregation.getBuckets().forEach(b -> {
					if (!isInList(items, b.getKeyAsString())) {
						items.add(new BucketItem(b.getKey(), b.getDocCount(), false));
					}
				});
			}

			// Arrange bucket size
			int bucketSize = getBucketSizeForAggregation(entry.getKey());
			boolean more = items.size() > bucketSize;
			buckets.add(new Bucket(entry.getKey(), (more ? items.subList(0, bucketSize) : items), more, aggregationType.getFilterType(), aggregationType.getRenderType()));
		});

		return buckets;
	}

	@Override
	public ShipmentsSearchResult extract(SearchResponse searchResponse) {
		ShipmentsSearchResult shipmentSearchResult = new ShipmentsSearchResult();

		// Extract shipments
		if (StringUtils.isBlank(config.getGroup())) {
			Page<ShipmentSearchDocument> shipments = new DefaultResultMapper().mapResults(searchResponse, ShipmentSearchDocument.class, new PageRequest(config.getPage() - 1, config.getSize()));
			shipmentSearchResult.setShipments(shipments.getContent());
			shipmentSearchResult.setTotalElements(shipments.getTotalElements());
			shipmentSearchResult.setTotalPages(shipments.getTotalPages());
		} else {
			List<ShipmentSearchDocument> items = new ArrayList<ShipmentSearchDocument>();

			((StringTerms) searchResponse.getAggregations().get(GroupType.GROUP_AGGREGATION_NAME)).getBuckets().forEach(bucket -> {
				List<ShipmentSearchDocument> documents = new ArrayList<>();

				SearchHits subHits = ((InternalTopHits) bucket.getAggregations().asList().get(0)).getHits();
				subHits.iterator().forEachRemaining(subHit -> {
					try {
						documents.add(new DefaultEntityMapper().mapToObject(subHit.getSourceAsString(), ShipmentSearchDocument.class));
					} catch (IOException e) {
						// Ignore the exception
					}
				});

				items.addAll(documents);
			});

			int start = (config.getPage() - 1) * config.getSize();
			int end = start + config.getSize();
			if (end > items.size()) {
				end = items.size();
			}

			Page<ShipmentSearchDocument> page = new PageImpl<ShipmentSearchDocument>(items.subList(start, end), new PageRequest(config.getPage() - 1, config.getSize()), items.size());
			shipmentSearchResult.setShipments(page.getContent());
			shipmentSearchResult.setTotalElements(page.getTotalElements());
			shipmentSearchResult.setTotalPages(page.getTotalPages());
		}



		//Extract aggregations
		Map<String, StringTerms> aggregations = new LinkedHashMap<>();
		searchQuery.getAggregations().forEach(agg -> {
			Aggregation aggregation = searchResponse.getAggregations().get(agg.getName());
			if (aggregation != null) {
				aggregations.put(agg.getName(), extractAggregation(aggregation, StringTerms.class));
			}
		});
		shipmentSearchResult.getFilter().setBuckets(flattenAggregations(aggregations));
		Arrays.stream(shipmentSearchResult.getFilter().getRangeTypes()).forEach(r->{
			r.refreshPresets();
					
			Optional<RangeFilter> range = config.getRangeFilters()
					.parallelStream()
					.filter(f->f.getName().equals(r.getName()))
					.findFirst();
			if(range.isPresent() && null != range.get().getPresetName()) {
				Optional<RangeTypePreset> preset = r.getPresets()
						.parallelStream()
						.filter(p->p.getName().equals(range.get().getPresetName()))
						.findFirst();
				if(preset.isPresent()) {
					preset.get().setSelected(true);
				}
			}
		});

		// Set config
		shipmentSearchResult.setConfig(config);

		return shipmentSearchResult;
	}

	public ShipmentsSearchResult getSearchResult(String q) {
		init();
		addAggregations();
		addGroupSorts();
		addSorts();
		return query(q);
	}
}
