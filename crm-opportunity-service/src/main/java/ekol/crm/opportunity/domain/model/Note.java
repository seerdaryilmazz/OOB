package ekol.crm.opportunity.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.model.CodeNamePair;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by Dogukan Sahinturk on 21.11.2019
 */
@Entity
@Table(name = "OpportunityNote")
@SequenceGenerator(name = "SEQ_OPPORTUNITYNOTE", sequenceName = "SEQ_OPPORTUNITYNOTE")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Note extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_OPPORTUNITYNOTE")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "opportunity_id")
    private Opportunity opportunity;

    @Column
    private String noteId;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="code", column=@Column(name="NOTE_TYPE_CODE")),
            @AttributeOverride(name = "name", column=@Column(name="NOTE_TYPE_NAME"))})
    private CodeNamePair type;

    @Column
    private String createdBy;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name= "dateTime", column= @Column (name = "CREATE_DATE"))
    })
    private UtcDateTime createDate;
}
