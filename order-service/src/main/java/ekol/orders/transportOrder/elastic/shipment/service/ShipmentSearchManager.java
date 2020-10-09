package ekol.orders.transportOrder.elastic.shipment.service;

import ekol.exceptions.BadRequestException;
import ekol.orders.transportOrder.elastic.shipment.config.AggregationFilter;
import ekol.orders.transportOrder.elastic.shipment.config.ShipmentSearchConfig;
import ekol.orders.transportOrder.elastic.shipment.document.ShipmentDocument;
import ekol.orders.transportOrder.elastic.shipment.model.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ozer on 10/10/16.
 */
public class ShipmentSearchManager implements ResultsExtractor<ShipmentSearchResult> {

    private static final String GLOBAL_AGGREGATION_PREFIX = "All ";

    private ShipmentSearchConfig config;
    private NativeSearchQueryBuilder queryBuilder;
    private SearchQuery searchQuery;
    private ElasticsearchTemplate elasticsearchTemplate;

    private void init() {
        queryBuilder = new NativeSearchQueryBuilder().withIndices(ShipmentDocument.INDEX_NAME);

        if (StringUtils.isBlank(config.getGroup())) {
            queryBuilder.withPageable(new PageRequest(config.getPage() - 1, config.getSize()));
        } else {
            // We won't use search hits
            queryBuilder.withPageable(new PageRequest(0, 1));
        }
    }

    private void addAggregations() {
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
        if (config.getAggregationFilters() != null) {
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
        List<SortBuilder> sorts = getSorts();
        if (sorts != null && !sorts.isEmpty()) {
            sorts.forEach(sort -> {
                queryBuilder.withSort(sort);
            });
        }
    }

    private ShipmentSearchResult query() {
        searchQuery = queryBuilder.withQuery(getFilters(null)).build();
        return elasticsearchTemplate.query(searchQuery, this);
    }

    private StringTerms extractAggregation(Aggregation aggregation) {
        if (aggregation instanceof StringTerms) {
            return (StringTerms) aggregation;
        } else if (aggregation instanceof InternalSingleBucketAggregation) {
            InternalSingleBucketAggregation complexAggregation = (InternalSingleBucketAggregation) aggregation;
            return extractAggregation(complexAggregation.getAggregations().asList().get(0));
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
            entry.getValue().getBuckets().forEach(b -> items.add(new BucketItem(b.getKey(), b.getDocCount(), aggregationFilter != null)));

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
            buckets.add(new Bucket(entry.getKey(), (more ? items.subList(0, bucketSize) : items), more, aggregationType.getFilterType()));
        });

        return buckets;
    }

    public ShipmentSearchManager(ShipmentSearchConfig config, ElasticsearchTemplate elasticsearchTemplate) {
        this.config = config;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public ShipmentSearchResult extract(SearchResponse searchResponse) {
        ShipmentSearchResult shipmentSearchResult = new ShipmentSearchResult();

        // Extract shipments
        if (StringUtils.isBlank(config.getGroup())) {
            Page<ShipmentDocument> shipments = new DefaultResultMapper().mapResults(searchResponse, ShipmentDocument.class, new PageRequest(config.getPage() - 1, config.getSize()));
            shipmentSearchResult.getShipments().add(shipments.getContent());
            shipmentSearchResult.setTotalElements(shipments.getTotalElements());
            shipmentSearchResult.setTotalPages(shipments.getTotalPages());
        } else {
            List<List<ShipmentDocument>> items = new ArrayList<List<ShipmentDocument>>();

            ((StringTerms) searchResponse.getAggregations().get(GroupType.GROUP_AGGREGATION_NAME)).getBuckets().forEach(bucket -> {
                List<ShipmentDocument> documents = new ArrayList<>();

                SearchHits subHits = ((InternalTopHits) bucket.getAggregations().asList().get(0)).getHits();
                subHits.iterator().forEachRemaining(subHit -> {
                    try {
                        documents.add(new DefaultEntityMapper().mapToObject(subHit.getSourceAsString(), ShipmentDocument.class));
                    } catch (IOException e) {
                        // Ignore the exception
                    }
                });

                items.add(documents);
            });

            int start = (config.getPage() - 1) * config.getSize();
            int end = start + config.getSize();
            if (end > items.size()) {
                end = items.size();
            }

            Page<List<ShipmentDocument>>  page = new PageImpl(items.subList(start, end), new PageRequest(config.getPage() - 1, config.getSize()), items.size());
            shipmentSearchResult.setShipments(page.getContent());
            shipmentSearchResult.setTotalElements(page.getTotalElements());
            shipmentSearchResult.setTotalPages(page.getTotalPages());
        }

        //Extract aggregations
        Map<String, StringTerms> aggregations = new LinkedHashMap<>();
        searchQuery.getAggregations().forEach(agg -> {
            Aggregation aggregation = searchResponse.getAggregations().get(agg.getName());
            if (aggregation != null) {
                aggregations.put(agg.getName(), extractAggregation(aggregation));
            }
        });
        shipmentSearchResult.setBuckets(flattenAggregations(aggregations));

        // Set config
        shipmentSearchResult.setConfig(config);

        return shipmentSearchResult;
    }

    public ShipmentSearchResult getSearchResult() {
        init();
        addAggregations();
        addSorts();
        return query();
    }
}
