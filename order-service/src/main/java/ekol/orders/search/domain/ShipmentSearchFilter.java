package ekol.orders.search.domain;

import java.util.List;

import ekol.orders.search.config.Bucket;
import ekol.orders.search.type.MatchType;
import ekol.orders.search.type.RangeType;
import ekol.orders.search.type.SortType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentSearchFilter {

    private List<Bucket> buckets;
    private RangeType[] rangeTypes = RangeType.values();
    private SortType[] sortTypes = SortType.values();
    private MatchType[] matchTypes = MatchType.values();
}
