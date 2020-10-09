package ekol.crm.quote.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PriceCalculation {

    @Column
    private BigDecimal minAmount;

    @Column
    private BigDecimal calculatedAmount;

    @OneToMany(mappedBy="price", fetch = FetchType.EAGER)
    @Where(clause="deleted=0")
    @JsonManagedReference
    private List<PriceDiscount> discounts = new ArrayList<>();
}
