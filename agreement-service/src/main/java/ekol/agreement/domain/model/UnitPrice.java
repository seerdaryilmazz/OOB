package ekol.agreement.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.agreement.domain.enumaration.BasedOnType;
import ekol.agreement.domain.enumaration.RenewalDateType;
import ekol.agreement.domain.model.agreement.Agreement;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "AgreementUnitPrice")
@SequenceGenerator(name = "SEQ_UNITPRICE", sequenceName = "SEQ_UNITPRICE")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@AuditTable("AUD_AGRMNT_UNIT_PRICE")
@Audited
@Where(clause = "deleted = 0")
public class UnitPrice extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_UNITPRICE")
    private Long id;

    @Column
    private String billingItem;

    @Column(nullable = false)
    private String serviceName;

    @Column(precision = 19, scale = 6)
    private BigDecimal price;

    @Column(length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    private BasedOnType basedOn;

    @Column(precision = 23, scale = 6)
    private BigDecimal eurRef;

    @Column(precision = 23, scale = 6)
    private BigDecimal usdRef;

    @Column(scale = 2)
    private BigDecimal minimumWageRef;

    @Column(precision = 3)
    private Integer inflationRef;

    @Column(nullable = false)
    private LocalDate validityStartDate;

    @Column
    private Integer updatePeriod;

    @Enumerated(EnumType.STRING)
    private RenewalDateType renewalDateType;

    @Column(nullable = false)
    private LocalDate validityEndDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "priceModel_id")
    private PriceAdaptationModel priceModel;

    @Column
    private String notes;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;
}
