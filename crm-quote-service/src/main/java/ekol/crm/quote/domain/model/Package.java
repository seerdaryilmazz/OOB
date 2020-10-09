package ekol.crm.quote.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.crm.quote.domain.model.quote.Quote;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Entity
@Table(name = "CrmPackage")
@SequenceGenerator(name = "SEQ_CRMPACKAGE", sequenceName = "SEQ_CRMPACKAGE")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Package extends AuditedBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMPACKAGE")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @Column
    private String type;

    @Column
    private Integer quantity;

    @Embedded
    private Dimension dimension;

    @Embedded
    private Measurement measurement;

    @Column
    private String stackabilityType;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hangingGoodsCategoryId")
    private HangingGoodsCategory hangingGoodsCategory;

}
