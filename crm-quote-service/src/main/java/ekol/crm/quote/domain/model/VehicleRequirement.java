package ekol.crm.quote.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.model.CodeNamePair;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "CrmVehicleReq")
@SequenceGenerator(name = "SEQ_CRMVEHICLEREQ", sequenceName = "SEQ_CRMVEHICLEREQ")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class VehicleRequirement extends AuditedBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMVEHICLEREQ")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="code", column=@Column(name="REQUIREMENT_CODE")),
            @AttributeOverride(name = "name", column=@Column(name="REQUIREMENT_NAME"))})
    private CodeNamePair requirement;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="code", column=@Column(name="OPERATION_CODE")),
            @AttributeOverride(name = "name", column=@Column(name="OPERATION_NAME"))})
    private CodeNamePair operationType;

    @Column
    private boolean removable ;

}
