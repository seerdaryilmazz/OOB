package ekol.crm.account.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "CrmAccountContact")
@SequenceGenerator(name = "SEQ_CRMACCOUNTCONTACT", sequenceName = "SEQ_CRMACCOUNTCONTACT")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Contact extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMACCOUNTCONTACT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false)
    private Long companyContactId;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Transient
    public String getFullName() {
    	return getFirstName() + " " + getLastName();
    }
}
