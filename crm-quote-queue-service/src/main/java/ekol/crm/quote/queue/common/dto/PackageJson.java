package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PackageJson {

    private Long id;
    private String type;
    private Integer quantity;
    private DimensionJson dimension;
    private MeasurementJson measurement;
    private String stackabilityType;
    private HangingGoodsCategoryJson hangingGoodsCategory;

}
