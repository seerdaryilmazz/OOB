package ekol.agreement.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "AgreementDocument")
@SequenceGenerator(name = "SEQ_AGREEMENTDOCUMENT", sequenceName = "SEQ_AGREEMENTDOCUMENT")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Document extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AGREEMENTDOCUMENT")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

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
