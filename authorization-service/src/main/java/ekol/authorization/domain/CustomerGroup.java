package ekol.authorization.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.authorization.domain.auth.AuthCustomerGroup;
import ekol.authorization.dto.IdNamePair;
import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name = "CustomerGroup")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerGroup extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1497259328484568379L;

	@Id
	@SequenceGenerator(name = "seq_customer_group", sequenceName = "seq_customer_group")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_customer_group")
	private Long id;

	private String name;

	@Embedded
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CustomerGroupCompany", joinColumns = @JoinColumn(name = "customerGroupId"))
	@AttributeOverrides({
		@AttributeOverride(name = "id", column = @Column(name = "id")),
		@AttributeOverride(name = "name", column = @Column(name = "name"))
	})
	private Set<IdNamePair> companies = new HashSet<>();

	public AuthCustomerGroup toAuthCustomerGroup() {
		AuthCustomerGroup authCustomerGroup = new AuthCustomerGroup();
		authCustomerGroup.setExternalId(getId());
		authCustomerGroup.setName(getName());
		return  authCustomerGroup;
	}
}
