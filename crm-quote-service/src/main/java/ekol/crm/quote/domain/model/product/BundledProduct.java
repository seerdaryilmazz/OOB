package ekol.crm.quote.domain.model.product;

import java.util.*;

import javax.persistence.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.*;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ekol.crm.quote.client.KartoteksService;
import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.product.*;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.MonetaryAmount;
import ekol.crm.quote.domain.model.businessVolume.BusinessVolume;
import ekol.crm.quote.util.BeanUtils;
import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import lombok.*;

@Entity
@Table(name = "CrmBundledProduct")
@PrimaryKeyJoinColumn(name = "product_id")
@Getter
@Setter
@NoArgsConstructor
@Audited
public class BundledProduct extends Product {

	@Enumerated(EnumType.STRING)
	private UnitOfMeasure unitOfMeasure;

	@Column
	private Integer quantity;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "amount", column = @Column(name = "priceOrgAmount")),
		@AttributeOverride(name = "currency", column = @Column(name = "priceOrgCurrency"))
	})
	private MonetaryAmount priceOriginal;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "amount", column = @Column(name = "priceExcAmount")),
		@AttributeOverride(name = "currency", column = @Column(name = "priceExcCurrency"))
	})
	private MonetaryAmount priceExchanged;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "amount", column = @Column(name = "expTurnoverOrgAmount")),
		@AttributeOverride(name = "currency", column = @Column(name = "expTurnoverOrgCurrency"))
	})
	private MonetaryAmount expectedTurnoverOriginal;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "amount", column = @Column(name = "expTurnoverExcAmount")),
		@AttributeOverride(name = "currency", column = @Column(name = "expTurnoverExcCurrency"))
	})
	private MonetaryAmount expectedTurnoverExchanged;

	@Column
	private String customsServiceType;
	
	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="id", column=@Column(name="CUSTOMS_OFFICE_ID")),
			@AttributeOverride(name = "name", column=@Column(name="CUSTOMS_OFFICE_NAME"))})
	private IdNamePair customsOffice;

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CrmBundleProdVhType", joinColumns = @JoinColumn(name = "productId"))
	private Set<VehicleType> vehicleType = new HashSet<>();
	
	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="iso", column=@Column(name="COUNTRY_ISO")),
			@AttributeOverride(name = "name", column=@Column(name="COUNTRY_NAME"))})
	private IsoNamePair country;
	
	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="id", column=@Column(name="POINT_ID")),
			@AttributeOverride(name = "name", column=@Column(name="POINT_NAME"))})
	private IdNamePair point;
	
	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="id", column=@Column(name="CROSS_DOCK_ID")),
			@AttributeOverride(name = "name", column=@Column(name="CROSS_DOCK_NAME"))})
	private IdNamePair crossDock;

	@Override
	public ProductJson toJson(){
		BundledProductJson json = new BundledProductJson();
		json.setId(getId());
		json.setFromCountry(getFromCountry());
		json.setFromPoint(getFromPoint());
		json.setToCountry(getToCountry());
		json.setToPoint(getToPoint());
		json.setShipmentLoadingType(getShipmentLoadingType());
		json.setIncoterm(getIncoterm());
		json.setIncotermExplanation(getIncotermExplanation());
		json.setCalculationType(getCalculationType());
		json.setStatus(getStatus());
		json.setUnitOfMeasure(getUnitOfMeasure());
		json.setQuantity(getQuantity());
		json.setPriceOriginal(Optional.ofNullable(getPriceOriginal()).map(MonetaryAmountJson::fromEntity).orElse(null));
		json.setPriceExchanged(Optional.ofNullable(getPriceExchanged()).map(MonetaryAmountJson::fromEntity).orElse(null));
		json.setExpectedTurnoverOriginal(Optional.ofNullable(getExpectedTurnoverOriginal()).map(MonetaryAmountJson::fromEntity).orElse(null));
		json.setExpectedTurnoverExchanged(Optional.ofNullable(getExpectedTurnoverExchanged()).map(MonetaryAmountJson::fromEntity).orElse(null));
		json.setExistence(getExistence());
		json.setCustomsServiceType(getCustomsServiceType());
		json.setCustomsOffice(getCustomsOffice());
		json.setLostReason(Optional.ofNullable(getLostReason()).map(LostReasonJson::fromEntity).orElse(null));
		json.setServiceArea(BeanUtils.getBean(KartoteksService.class).findServiceAreaByCode(getServiceArea(),true));
		json.setVehicleType(getVehicleType());
		json.setDiscriminator("BUNDLED");
		json.setLastUpdated(getLastUpdated());
		json.setLastUpdatedBy(getLastUpdatedBy());
		json.setDeletedAt(getDeletedAt());
		json.setCountry(getCountry());
		json.setPoint(getPoint());
		json.setCrossDock(getCrossDock());

		return json;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(getUnitOfMeasure())
				.append(getQuantity())
				.append(getPriceOriginal())
				.append(getPriceExchanged())
				.append(getExpectedTurnoverOriginal())
				.append(getExpectedTurnoverExchanged())
				.append(getCustomsServiceType())
				.append(getCustomsOffice())
				.append(getVehicleType())
				.append(getCountry())
				.append(getPoint())
				.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof BundledProduct))
			return false;
		if (object == this)
			return true;

		BundledProduct entity = BundledProduct.class.cast(object);
		if(!super.equals(object)) {
			return false;
		}
		return new EqualsBuilder()
				.appendSuper(CollectionUtils.isEqualCollection(entity.getVehicleType(), getVehicleType()))
				.append(entity.getUnitOfMeasure(), getUnitOfMeasure())
				.append(entity.getQuantity(), getQuantity())
				.append(entity.getPriceOriginal(), getPriceOriginal())
				.append(entity.getPriceExchanged(), getPriceExchanged())
				.append(entity.getExpectedTurnoverOriginal(), getExpectedTurnoverOriginal())
				.append(entity.getExpectedTurnoverExchanged(), getExpectedTurnoverExchanged())
				.append(entity.getCustomsServiceType(), getCustomsServiceType())
				.append(entity.getCustomsOffice(), getCustomsOffice())
				.append(entity.getCountry(), getCountry())
				.append(entity.getPoint(), getPoint())
				.isEquals();
	}


}
