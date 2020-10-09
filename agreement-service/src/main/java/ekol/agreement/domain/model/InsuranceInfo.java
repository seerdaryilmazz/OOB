package ekol.agreement.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.agreement.domain.enumaration.InsuranceType;
import ekol.agreement.domain.enumaration.EkolOrCustomer;
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
@Table(name = "AgreementInsuranceInfo")
@SequenceGenerator(name = "SEQ_INSURANCEINFO", sequenceName = "SEQ_INSURANCEINFO")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@AuditTable("AUD_AGREEMENT_INS_INFO")
@Audited
@Where(clause = "deleted = 0")
@SQLDelete(sql = "UPDATE AGREEMENT_INSURANCE_INFO SET DELETED=1 WHERE ID=?")
public class InsuranceInfo extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INSURANCEINFO")
    private Long id;

    @Enumerated(EnumType.STRING)
    private InsuranceType insuranceType;

    @Column(precision = 11, scale = 2)
    private BigDecimal exemptionLimit;

    @Enumerated(EnumType.STRING)
    private EkolOrCustomer insuredBy;

    @Column(length = 3)
    private String currency;

    @Column(nullable = false)
    private LocalDate validityStartDate;

    @Column(nullable = false)
    private LocalDate validityEndDate;

    @Column(length = 50)
    private String coverage;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;
}
