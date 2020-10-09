package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.agreement.domain.model.KpiInfo;
import ekol.agreement.domain.enumaration.RenewalDateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KpiInfoJson {
    private Long id;
    @NotNull(message = "Kpi Info name can not be null")
    private String name;
    @NotNull(message = "Kpi Info target can not be null")
    private String target;
    private String actual;
    private LocalDate lastUpdateDate;
    private Integer updatePeriod;
    private RenewalDateType renewalDateType;
    private LocalDate nextUpdateDate;

    public KpiInfo toEntity() {
        return KpiInfo.builder()
                .id(getId())
                .name(getName())
                .target(getTarget())
                .actual(getActual())
                .lastUpdateDate(getLastUpdateDate())
                .updatePeriod(getUpdatePeriod())
                .renewalDateType(getRenewalDateType())
                .nextUpdateDate(getNextUpdateDate()).build();
    }

    public static KpiInfoJson fromEntity(KpiInfo kpiInfo) {
        return new KpiInfoJsonBuilder()
                .id(kpiInfo.getId())
                .name(kpiInfo.getName())
                .target(kpiInfo.getTarget())
                .actual(kpiInfo.getActual())
                .lastUpdateDate(kpiInfo.getLastUpdateDate())
                .updatePeriod(kpiInfo.getUpdatePeriod())
                .renewalDateType(kpiInfo.getRenewalDateType())
                .nextUpdateDate(kpiInfo.getNextUpdateDate()).build();
    }
}
