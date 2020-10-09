package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.model.Measurement;
import lombok.*;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementJson {

    private BigDecimal weight;
    private BigDecimal ldm;
    private BigDecimal volume;

    public Measurement toEntity(){
        return Measurement.builder()
                .weight(getWeight())
                .ldm(getLdm())
                .volume(getVolume()).build();
    }

    public static MeasurementJson fromEntity(Measurement measurement){
        return new MeasurementJson.MeasurementJsonBuilder()
                .weight(measurement.getWeight())
                .ldm(measurement.getLdm())
                .volume(measurement.getVolume()).build();
    }

}
