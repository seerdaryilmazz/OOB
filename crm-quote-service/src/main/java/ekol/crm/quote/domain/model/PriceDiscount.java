package ekol.crm.quote.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "CrmPriceDiscount")
@SequenceGenerator(name = "SEQ_CRMPRICEDISCOUNT", sequenceName = "SEQ_CRMPRICEDISCOUNT")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class PriceDiscount extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMPRICEDISCOUNT")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "price_id")
    private Price price;

    @Column
    private Long salesPriceId;

    @Column
    private String name;

    @Column
    private BigDecimal percentage;

    @Column
    private BigDecimal amount;

}
