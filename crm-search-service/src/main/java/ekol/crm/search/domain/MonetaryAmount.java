package ekol.crm.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.search.event.dto.opportunity.MonetaryAmountJson;
import ekol.model.CodeNamePair;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

/**
 * Created by Dogukan Sahinturk on 27.01.2020
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonetaryAmount {
    @Field(type = FieldType.Double)
    private BigDecimal amount;
    @Field(type = FieldType.Nested)
    private CodeNamePair currency;

    public static MonetaryAmount fromMonetaryAmount(MonetaryAmountJson monetaryAmountJson){
        return new MonetaryAmountBuilder()
                .amount(monetaryAmountJson.getAmount())
                .currency(monetaryAmountJson.getCurrency())
                .build();
    }
}
