package ekol.orders.transportOrder.elastic.shipment.config;

import java.util.List;

/**
 * Created by ozer on 05/10/16.
 */
public class AggregationFilter {

    private String bucketName;
    private List<String> values;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
