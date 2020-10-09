package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.model.HangingGoodsCategory;
import ekol.crm.quote.domain.model.Package;
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
    private HangingGoodsCategory hangingGoodsCategory;

    public Package toEntity(){
        return  Package.builder()
                .id(getId())
                .type(getType())
                .quantity(getQuantity())
                .dimension(getDimension() != null ? getDimension().toEntity(): null)
                .measurement(getMeasurement() != null ? getMeasurement().toEntity(): null)
                .stackabilityType(getStackabilityType())
                .hangingGoodsCategory(getHangingGoodsCategory())
                .build();
    }

    public static PackageJson fromEntity(Package quotePackage) {
        return new PackageJsonBuilder()
                .id(quotePackage.getId())
                .type(quotePackage.getType())
                .quantity(quotePackage.getQuantity())
                .dimension(quotePackage.getDimension() != null ? DimensionJson.fromEntity(quotePackage.getDimension()): null)
                .measurement(quotePackage.getMeasurement() != null ? MeasurementJson.fromEntity(quotePackage.getMeasurement()): null)
                .stackabilityType(quotePackage.getStackabilityType())
                .hangingGoodsCategory(quotePackage.getHangingGoodsCategory())
                .build();
    }
}
