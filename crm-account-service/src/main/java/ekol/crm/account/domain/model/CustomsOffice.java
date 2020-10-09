package ekol.crm.account.domain.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.model.IdNamePair;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;


@Entity
@Table(name = "CrmPtCustomsOffice")
@SequenceGenerator(name = "SEQ_CRMPOTENTIALCUSTOMSOFFICE", sequenceName = "SEQ_CRMPOTENTIALCUSTOMSOFFICE")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class CustomsOffice{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMPOTENTIALCUSTOMSOFFICE")
    private Long id;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="CUSTOMS_OFFICE_ID")),
            @AttributeOverride(name = "name", column=@Column(name="CUSTOMS_OFFICE_NAME"))})
    private IdNamePair office;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "potential_id")
    @JsonBackReference
    private Potential potential;

}
