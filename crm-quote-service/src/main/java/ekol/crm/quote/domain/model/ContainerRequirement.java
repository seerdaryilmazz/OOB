package ekol.crm.quote.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.model.CodeNamePair;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "CrmContainerReq")
@SequenceGenerator(name = "SEQ_CRMCONTAINERREQ", sequenceName = "SEQ_CRMCONTAINERREQ")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class ContainerRequirement extends AuditedBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMCONTAINERREQ")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="code", column=@Column(name="VOLUME_CODE")),
            @AttributeOverride(name = "name", column=@Column(name="VOLUME_NAME"))})
    private CodeNamePair volume;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="code", column=@Column(name="CONTAINER_CODE")),
            @AttributeOverride(name = "name", column=@Column(name="CONTAINER_NAME"))})
    private CodeNamePair type;

    @Column
    private Integer quantity;

}
