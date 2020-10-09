package ekol.agreement.domain.model;

import ekol.agreement.domain.enumaration.EkolOrCustomer;
import ekol.agreement.domain.model.agreement.Agreement;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "AgreementSignatureInfo")
@SequenceGenerator(name = "SEQ_SIGNATUREINFO", sequenceName = "SEQ_SIGNATUREINFO")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@AuditTable("AUD_AGRMNT_SIGN_INFO")
@Audited
@Where(clause = "deleted = 0")
@SQLDelete(sql = "UPDATE AGREEMENT_SIGNATURE_INFO SET DELETED=1 WHERE ID=?")
public class SignatureInfo extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SIGNATUREINFO")
    private Long id;

    @Enumerated(EnumType.STRING)
    private EkolOrCustomer signedBy;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String title;

    @Column(nullable = false)
    private LocalDate signedDate;

    @Column(length = 50)
    private String place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;
}
