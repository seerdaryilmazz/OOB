package ekol.crm.quote.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.crm.quote.domain.enumaration.LoadType;
import ekol.crm.quote.domain.model.quote.Quote;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "CrmLoad")
@SequenceGenerator(name = "SEQ_CRMLOAD", sequenceName = "SEQ_CRMLOAD")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Load extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMLOAD")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @Enumerated(EnumType.STRING)
    private LoadType type;

    @Column
    private String riskFactor;

    @Column
    private Integer minTemperature;

    @Column
    private Integer maxTemperature;

}
