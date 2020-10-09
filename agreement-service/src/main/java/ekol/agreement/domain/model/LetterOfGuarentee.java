package ekol.agreement.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.agreement.domain.model.agreement.Agreement;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder(toBuilder = true)
@SequenceGenerator(name = "SEQ_LETTEROFGUARENTEE", sequenceName = "SEQ_LETTEROFGUARENTEE")
@Table(name = "AgreementLetterGrnt")
@NoArgsConstructor
@AllArgsConstructor
@AuditTable("AUD_AGRMNT_LETTER_GRNT")
@Audited
@Where(clause = "deleted = 0")
@SQLDelete(sql = "UPDATE AGREEMENT_LETTER_GRNT SET DELETED=1 WHERE ID=?")
public class LetterOfGuarentee extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LETTEROFGUARENTEE")
    private Long id;

    private String scope;

    @Column(precision = 11, scale = 2)
    private BigDecimal contractAmount;

    @Column(length = 3)
    private String currency;

    @Column(nullable = false)
    private LocalDate validityStartDate;

    @Column(nullable = false)
    private LocalDate validityEndDate;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;
}
