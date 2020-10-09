package ekol.orders.transportOrder.elastic.shipment.model;

/**
 * Created by ozer on 03/10/16.
 */
public class BucketItem {

    private Object key;
    private long count;
    private boolean selected;

    public BucketItem(Object key, long count, boolean selected) {
        this.key = key;
        this.count = count;
        this.selected = selected;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
