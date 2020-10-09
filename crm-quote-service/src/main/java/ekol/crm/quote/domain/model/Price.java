package ekol.crm.quote.domain.model;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.hibernate.envers.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ekol.crm.quote.domain.enumaration.PriceType;
import ekol.crm.quote.domain.model.quote.Quote;
import lombok.*;

@Entity
@Table(name = "CrmPrice")
@SequenceGenerator(name = "SEQ_CRMPRICE", sequenceName = "SEQ_CRMPRICE")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Price extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMPRICE")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @Enumerated(EnumType.STRING)
    private PriceType type;

//    TODO: https://hibernate.atlassian.net/browse/HHH-8066
//    Audit record düzgün şekilde oluşturulamıyor, bu yüzden entity join yapılmadı.
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name="billing_item", referencedColumnName = "name")
//    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
//    private BillingItem billingItem;
    private String billingItem;

    @Column(nullable = false)
    private boolean addToFreight;

    @Embedded
    private MonetaryAmount charge;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "exchangeAmount")),
            @AttributeOverride(name = "currency", column = @Column(name = "exchangeCurrency"))
    })
    private MonetaryAmount priceExchanged;

    @Embedded
    private PriceCalculation priceCalculation;

    private String campaignId;
    
    @NotAudited
    @OneToOne(mappedBy = "price", fetch = FetchType.EAGER)
    private PriceAuthorization authorization;
}
