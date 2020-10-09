package ekol.crm.quote.domain.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.model.CodeNamePair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CrmNote")
@SequenceGenerator(name = "SEQ_CRMNOTE", sequenceName = "SEQ_CRMNOTE")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Note extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMNOTE")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "quote_id")
    private Quote quote;

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
            @AttributeOverride(name= "dateTime", column= @Column (name = "CREATED_AT"))
    })
    private UtcDateTime createDate;
}
