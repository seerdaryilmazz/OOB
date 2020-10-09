package ekol.crm.quote.domain.model.product;

import java.util.Objects;

import javax.persistence.*;

import org.apache.commons.lang3.builder.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ekol.crm.quote.common.Constants;
import ekol.crm.quote.domain.dto.product.ProductJson;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.crm.quote.util.ProductUtils;
import ekol.model.*;
import lombok.*;

@Entity
@Table(name = "CrmProduct")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "SEQ_CRMPRODUCT", sequenceName = "SEQ_CRMPRODUCT")
@Where(clause = "deleted = 0")
@Getter
@Setter
@NoArgsConstructor
@Audited
public abstract class Product extends AuditedBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMPRODUCT")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonBackReference
	@JoinColumn(name = "quote_id")
	private Quote quote;

	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="iso", column=@Column(name="FROM_COUNTRY_ISO")),
			@AttributeOverride(name = "name", column=@Column(name="FROM_COUNTRY_NAME"))})
	private IsoNamePair fromCountry;

	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="id", column=@Column(name="FROM_POINT_ID")),
			@AttributeOverride(name = "name", column=@Column(name="FROM_POINT_NAME"))})
	private IdNamePair fromPoint;

	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="iso", column=@Column(name="TO_COUNTRY_ISO")),
			@AttributeOverride(name = "name", column=@Column(name="TO_COUNTRY_NAME"))})
	private IsoNamePair toCountry;

	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="id", column=@Column(name="TO_POINT_ID")),
			@AttributeOverride(name = "name", column=@Column(name="TO_POINT_NAME"))})
	private IdNamePair toPoint;

	@Column
	private String shipmentLoadingType;

	@Column
	private String incoterm;

	@Column
	private String incotermExplanation;

	@Enumerated(EnumType.STRING)
	private CalculationType calculationType;

	@Enumerated(EnumType.STRING)
	private ProductStatus status;

	@Enumerated(EnumType.STRING)
	private ExistenceType existence;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lostReasonId")
	private LostReason lostReason;

	@Column
	private String serviceArea;

	public abstract ProductJson toJson();

	@Transient
	public boolean isExport() {
		return ProductUtils.isExport(this);
	}

	@Transient
	public boolean isImport() {
		return ProductUtils.isImport(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getId())
				.append(getFromCountry())
				.append(getFromPoint())
				.append(getToCountry())
				.append(getToPoint())
				.append(getShipmentLoadingType())
				.append(getIncoterm())
				.append(getIncotermExplanation())
				.append(getCalculationType())
				.append(getStatus())
				.append(getExistence())
				.append(getServiceArea())
				.append(getLostReason())
				.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Product))
			return false;
		if (object == this)
			return true;

		Product entity = Product.class.cast(object);
		return new EqualsBuilder()
				.append(entity.getId(), getId())
				.append(entity.getFromCountry(), getFromCountry())
				.append(entity.getFromPoint(), getFromPoint())
				.append(entity.getToCountry(), getToCountry())
				.append(entity.getToPoint(), getToPoint())
				.append(entity.getShipmentLoadingType(), getShipmentLoadingType())
				.append(entity.getIncoterm(), getIncoterm())
				.append(entity.getIncotermExplanation(), getIncotermExplanation())
				.append(entity.getCalculationType(), getCalculationType())
				.append(entity.getStatus(), getStatus())
				.append(entity.getExistence(), getExistence())
				.append(entity.getServiceArea(), getServiceArea())
				.append(entity.getLostReason(), getLostReason())
				.isEquals();
	}

}
