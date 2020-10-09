package ekol.orders.transportOrder.elastic.shipment.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ozer on 05/10/16.
 */
public class ShipmentSearchConfig {

    // Default values are the same as ShipmentSearch.jsx

    private int page = 1;
    private int size = 4;
    private List<AggregationFilter> aggregationFilters = new ArrayList<>();
    private Map<String, Integer> bucketSizes = new HashMap<>();
    private List<RangeFilter> rangeFilters = new ArrayList<>();
    private List<Sort> sorts = new ArrayList<>();
    private String group;
    private int aggregationBucketSize = 3;
    private int groupBucketSize = 10;
    private List<MatchFilter> matchFilters = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<AggregationFilter> getAggregationFilters() {
        return aggregationFilters;
    }

    public void setAggregationFilters(List<AggregationFilter> aggregationFilters) {
        this.aggregationFilters = aggregationFilters;
    }

    public Map<String, Integer> getBucketSizes() {
        return bucketSizes;
    }

    public void setBucketSizes(Map<String, Integer> bucketSizes) {
        this.bucketSizes = bucketSizes;
    }

    public List<RangeFilter> getRangeFilters() {
        return rangeFilters;
    }

    public void setRangeFilters(List<RangeFilter> rangeFilters) {
        this.rangeFilters = rangeFilters;
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getAggregationBucketSize() {
        return aggregationBucketSize;
    }

    public void setAggregationBucketSize(int aggregationBucketSize) {
        this.aggregationBucketSize = aggregationBucketSize;
    }

    public int getGroupBucketSize() {
        return groupBucketSize;
    }

    public void setGroupBucketSize(int groupBucketSize) {
        this.groupBucketSize = groupBucketSize;
    }

    public List<MatchFilter> getMatchFilters() {
        return matchFilters;
    }

    public void setMatchFilters(List<MatchFilter> matchFilters) {
        this.matchFilters = matchFilters;
    }

    public AggregationFilter findAggregationFilterForAggregationType(String aggregationType) {
        if (aggregationFilters != null && !aggregationFilters.isEmpty()) {
            List<AggregationFilter> aggregationFilterList = aggregationFilters
                    .stream()
                    .filter(aggregationFilter -> aggregationFilter.getBucketName().equals(aggregationType))
                    .collect(Collectors.toList());

            if (aggregationFilterList != null && !aggregationFilterList.isEmpty()) {
                return aggregationFilterList.get(0);
            }
        }

        return null;
    }
}