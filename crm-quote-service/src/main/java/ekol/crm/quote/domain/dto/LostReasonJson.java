package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.model.LostReason;
import ekol.crm.quote.domain.enumaration.LostReasonType;
import ekol.exceptions.ValidationException;
import ekol.model.IdNamePair;
import lombok.*;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LostReasonJson {

    private Long id;
    private IdNamePair competitor;
    private BigDecimal competitorPrice;
    private String competitorPriceCurrency;
    private LostReasonType reason;
    private String reasonDetail;

    public LostReason toEntity(){
        return LostReason.builder()
                .id(getId())
                .competitor(getCompetitor())
                .competitorPrice(getCompetitorPrice())
                .competitorPriceCurrency(getCompetitorPriceCurrency())
                .reason(getReason())
                .reasonDetail(getReasonDetail()).build();
    }

    public static LostReasonJson fromEntity(LostReason lostReason){
        return new LostReasonJson.LostReasonJsonBuilder()
                .id(lostReason.getId())
                .competitor(lostReason.getCompetitor())
                .competitorPrice(lostReason.getCompetitorPrice())
                .competitorPriceCurrency(lostReason.getCompetitorPriceCurrency())
                .reason(lostReason.getReason())
                .reasonDetail(lostReason.getReasonDetail()).build();
    }

    public void validate(){
        if(getReason() == null){
            throw new ValidationException("Lost quote should have a reason");
        }
    }

}
