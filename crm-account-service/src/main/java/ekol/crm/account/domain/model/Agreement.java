package ekol.crm.account.domain.model;

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
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CrmAccountAgreement")
@SequenceGenerator(name = "SEQ_CRMACCOUNTAGREEMENT", sequenceName = "SEQ_CRMACCOUNTAGREEMENT")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Agreement extends AuditedBaseEntity{
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMACCOUNTAGREEMENT")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

}
