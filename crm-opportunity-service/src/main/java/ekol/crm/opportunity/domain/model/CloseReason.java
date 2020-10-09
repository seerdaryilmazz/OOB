package ekol.crm.opportunity.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.crm.opportunity.domain.enumaration.CloseReasonType;
import ekol.crm.opportunity.domain.enumaration.CloseType;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.model.IdNamePair;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Dogukan Sahinturk on 25.11.2019
 */
@Entity
@Table(name = "OpportunityCloseReason")
@SequenceGenerator(name = "SEQ_OPPCLOSEREASON", sequenceName = "SEQ_OPPCLOSEREASON")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CloseReason extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_OPPCLOSEREASON")
    private Long id;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id")
    private Opportunity opportunity;

    @Enumerated(EnumType.STRING)
    private CloseType type;

    @Enumerated(EnumType.STRING)
    private CloseReasonType reason;

    @Column
    private String reasonDetail;
}
