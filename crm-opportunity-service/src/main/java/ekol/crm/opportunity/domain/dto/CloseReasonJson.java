package ekol.crm.opportunity.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.opportunity.domain.enumaration.CloseReasonType;
import ekol.crm.opportunity.domain.enumaration.CloseType;
import ekol.crm.opportunity.domain.model.CloseReason;
import ekol.model.IdNamePair;
import lombok.*;

import java.math.BigDecimal;

/**
 * Created by Dogukan Sahinturk on 25.11.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CloseReasonJson {

    private Long id;
    private CloseType type;
    private CloseReasonType reason;
    private String reasonDetail;

    public CloseReason toEntity(){
        return CloseReason.builder()
                .id(getId())
                .type(getType())
                .reason(getReason())
                .reasonDetail(getReasonDetail()).build();
    }

    public static CloseReasonJson fromEntity(CloseReason closeReason){
        return new CloseReasonJsonBuilder()
                .id(closeReason.getId())
                .type(closeReason.getType())
                .reason(closeReason.getReason())
                .reasonDetail(closeReason.getReasonDetail()).build();
    }
}
