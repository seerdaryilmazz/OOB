package ekol.agreement.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.model.CodeNamePair;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "AgreementNote")
@SequenceGenerator(name = "SEQ_AGREEMENTNOTE", sequenceName = "SEQ_AGREEMENTNOTE")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Note extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AGREEMENTNOTE")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

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
