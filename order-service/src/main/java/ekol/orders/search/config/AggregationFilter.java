package ekol.orders.search.config;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregationFilter {

    private String bucketName;
    private List<String> values;

}
