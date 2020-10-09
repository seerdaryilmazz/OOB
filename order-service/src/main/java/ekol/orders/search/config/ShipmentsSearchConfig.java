package ekol.orders.search.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentsSearchConfig {

    // Default values are the same as ShipmentSearch.jsx

    private int page = 1;
    private int size = 4;
    private List<AggregationFilter> aggregationFilters = new ArrayList<>();
    private Map<String, Integer> bucketSizes = new HashMap<>();
    private List<RangeFilter> rangeFilters = new ArrayList<>();
    private List<Sort> sorts = new ArrayList<>();
    private List<Sort> groupSorts = new ArrayList<>();
    private String group;
    private int aggregationBucketSize = 3;
    private int groupBucketSize = 10;
    private List<MatchFilter> matchFilters = new ArrayList<>();

    public AggregationFilter findAggregationFilterForAggregationType(String aggregationType) {
        if (!CollectionUtils.isEmpty(aggregationFilters)) {
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