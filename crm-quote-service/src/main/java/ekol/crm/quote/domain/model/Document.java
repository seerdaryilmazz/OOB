package ekol.crm.quote.domain.model;

import java.time.*;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CrmDocument")
@SequenceGenerator(name = "SEQ_CRMDOCUMENT", sequenceName = "SEQ_CRMDOCUMENT")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Document extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMDOCUMENT")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @Column
    private String documentId;

    @Column
    private String documentName;

    @Column
    private boolean ineffaceable;

    @Column
    @CreatedBy
    private String createdBy;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name= "dateTime", column= @Column (name = "CREATED_AT"))
    })
    private UtcDateTime createDate;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CrmDocumentEmail", joinColumns = @JoinColumn(name = "documentId"))
    @Column(name="EMAIL_ID")
    private Set<String> emails;
    
    @Override
    @PrePersist
    public void prePersist() {
    	if (null == createDate && null == id) {
    		createDate = new UtcDateTime(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        }
        super.prePersist();
    }

}
