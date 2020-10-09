package ekol.orders.search.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RangeFilter {

    private String name;
    private String gte;
    private String lte;
    private String presetName;
}
