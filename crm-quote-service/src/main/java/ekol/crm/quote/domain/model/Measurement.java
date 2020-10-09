package ekol.crm.quote.domain.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {

    @Column
    private BigDecimal weight;

    @Column
    private BigDecimal ldm;

    @Column
    private BigDecimal volume;
}
