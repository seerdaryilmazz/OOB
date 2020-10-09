package ekol.orders.transportOrder.elastic.shipment.model;

import java.util.List;

/**
 * Created by ozer on 03/10/16.
 */
public class Bucket {

    private String name;
    private List<BucketItem> items;
    private boolean more;
    private AggregationFilterType aggregationFilterType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BucketItem> getItems() {
        return items;
    }

    public void setItems(List<BucketItem> items) {
        this.items = items;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public AggregationFilterType getAggregationFilterType() {
        return aggregationFilterType;
    }

    public void setAggregationFilterType(AggregationFilterType aggregationFilterType) {
        this.aggregationFilterType = aggregationFilterType;
    }

    public Bucket(String name, List<BucketItem> items, boolean more, AggregationFilterType aggregationFilterType) {
        this.name = name;
        this.items = items;
        this.more = more;
        this.aggregationFilterType = aggregationFilterType;
    }
}
