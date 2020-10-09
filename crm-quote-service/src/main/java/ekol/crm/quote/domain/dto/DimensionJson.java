package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.model.Dimension;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DimensionJson {

    private Integer width;
    private Integer length;
    private Integer height;

    public Dimension toEntity(){
        return Dimension.builder()
                .width(getWidth())
                .length(getLength())
                .height(getHeight()).build();
    }

    public static DimensionJson fromEntity(Dimension dimension){
        return new DimensionJson.DimensionJsonBuilder()
                .width(dimension.getWidth())
                .length(dimension.getLength())
                .height(dimension.getHeight()).build();
    }

}
