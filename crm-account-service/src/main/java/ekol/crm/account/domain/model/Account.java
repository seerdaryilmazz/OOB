package ekol.crm.account.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.enumaration.AccountType;
import ekol.crm.account.domain.enumaration.SegmentType;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;


@Entity
@Table(name = "CrmAccount")
@SequenceGenerator(name = "SEQ_CRMACCOUNT", sequenceName = "SEQ_CRMACCOUNT")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Account extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMACCOUNT")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="COMPANY_ID")),
            @AttributeOverride(name = "name", column=@Column(name="COMPANY_NAME"))})
    private IdNamePair company;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="iso", column=@Column(name="COUNTRY_ISO")),
            @AttributeOverride(name = "name", column=@Column(name="COUNTRY_NAME"))})
    private IsoNamePair country;

    @Column(length = 100)
    private String accountOwner;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private SegmentType segment;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="code", column=@Column(name="PARENT_SECTOR_CODE")),
            @AttributeOverride(name = "name", column=@Column(name="PARENT_SECTOR_NAME"))})
    private CodeNamePair parentSector;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="code", column=@Column(name="SUB_SECTOR_CODE")),
            @AttributeOverride(name = "name", column=@Column(name="SUB_SECTOR_NAME"))})
    private CodeNamePair subSector;

    @Embedded
    private AccountDetail details;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "createdAt"))
    })
    private UtcDateTime createdAt;

    @CreatedBy
    private String createdBy;

}
