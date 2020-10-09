package ekol.crm.quote.domain.model;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.hibernate.envers.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ekol.crm.quote.domain.model.quote.Quote;
import lombok.*;

@Entity
@Table(name = "CrmService")
@SequenceGenerator(name = "SEQ_CRMSERVICE", sequenceName = "SEQ_CRMSERVICE")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Service extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMSERVICE")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ServiceType type;


}
