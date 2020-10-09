package ekol.crm.quote.domain.model;

import ekol.crm.quote.domain.enumaration.PaymentType;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Entity
@Table(name = "CrmPaymentRule")
@SequenceGenerator(name = "SEQ_CRMPAYMENTRULE", sequenceName = "SEQ_CRMPAYMENTRULE")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor

@AllArgsConstructor
@Audited

public class PaymentRule extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMPAYMENTRULE")
    private Long id;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="INVOICE_COMPANY_ID")),
            @AttributeOverride(name = "name", column=@Column(name="INVOICE_COMPANY_NAME"))})
    private IdNamePair invoiceCompany;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="INVOICE_LOCATION_ID")),
            @AttributeOverride(name = "name", column=@Column(name="INVOICE_LOCATION_NAME"))})
    private IdNamePair invoiceLocation;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Column
    private Integer paymentDueDays;
    
    @OneToOne(fetch = FetchType.EAGER)
   	@JoinColumn(name = "ownerCompanyId")
    @Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
    private OwnerCompany ownerCompany;

}
