package ekol.orders.search.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BucketItem {

    private Object key;
    private long count;
    private boolean selected;

}
