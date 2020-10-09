package ekol.orders.search.config;

import java.util.List;

import ekol.orders.search.type.AggregationFilterType;
import ekol.orders.search.type.AggregationRenderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bucket {

    private String name;
    private List<BucketItem> items;
    private boolean more;
    private AggregationFilterType aggregationFilterType;
    private AggregationRenderType aggregationRenderType;

}
