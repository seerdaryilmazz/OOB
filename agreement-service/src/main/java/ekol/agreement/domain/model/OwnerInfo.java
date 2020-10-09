package ekol.agreement.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.agreement.domain.enumaration.ResponsibilityType;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AgreementOwnerInfo")
@SequenceGenerator(name = "SEQ_OWNERINFO", sequenceName = "SEQ_OWNERINFO")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AuditTable("AUD_AGRMNT_OWNER_INFO")
@Audited
@Where(clause = "deleted = 0")
@SQLDelete(sql = "UPDATE AGREEMENT_OWNER_INFO SET DELETED=1 WHERE ID=?")
public class OwnerInfo extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_OWNERINFO")
    private Long id;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="CONTACT_ID")),
            @AttributeOverride(name = "name", column=@Column(name="CONTACT_NAME"))})
    private IdNamePair name;

    @Enumerated(value = EnumType.STRING)
    private ResponsibilityType responsibilityType;

    @ElementCollection
    @CollectionTable(name = "Agrmnt_Owner_I_Service_A", joinColumns = @JoinColumn(name = "owner_info_id"))
    @AttributeOverrides({
            @AttributeOverride(name = "code", column = @Column(name = "CODE")),
            @AttributeOverride(name = "name", column = @Column(name = "NAME"))
    })
    private Set<CodeNamePair> serviceAreas = new HashSet<>();

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;
}
