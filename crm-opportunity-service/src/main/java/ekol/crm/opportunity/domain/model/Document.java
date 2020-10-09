package ekol.crm.opportunity.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by Dogukan Sahinturk on 21.11.2019
 */
@Entity
@Table(name = "OpportunityDocument")
@SequenceGenerator(name = "SEQ_OPPORTUNITYDOCUMENT", sequenceName = "SEQ_OPPORTUNITYDOCUMENT")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Document extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_OPPORTUNITYDOCUMENT")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "opportunity_id")
    private Opportunity opportunity;

    @Column
    private String documentId;

    @Column
    private String documentName;

    @Column
    private boolean ineffaceable;

    @Column
    private String createdBy;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name= "dateTime", column= @Column (name = "CREATE_DATE"))
    })
    private UtcDateTime createDate;

}
