package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.model.Load;
import ekol.crm.quote.domain.enumaration.LoadType;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoadJson {

    private Long id;
    private LoadType type;
    private String riskFactor;
    private Integer minTemperature;
    private Integer maxTemperature;

    public Load toEntity(){
        return Load.builder()
                .id(getId())
                .type(getType())
                .riskFactor(getRiskFactor())
                .minTemperature(getMinTemperature())
                .maxTemperature(getMaxTemperature()).build();
    }

    public static LoadJson fromEntity(Load load){
        return new LoadJsonBuilder()
                .id(load.getId())
                .type(load.getType())
                .riskFactor(load.getRiskFactor())
                .minTemperature(load.getMinTemperature())
                .maxTemperature(load.getMaxTemperature()).build();
    }

}
