package ekol.crm.quote.domain.model.product;

import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.*;

import org.apache.commons.lang3.builder.*;
import org.hibernate.envers.Audited;

import ekol.crm.quote.client.KartoteksService;
import ekol.crm.quote.domain.dto.LostReasonJson;
import ekol.crm.quote.domain.dto.product.*;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.util.BeanUtils;
import ekol.model.*;
import lombok.*;

@Entity
@Table(name = "CrmSpotProduct")
@PrimaryKeyJoinColumn(name = "product_id")
@Getter
@Setter
@NoArgsConstructor
@Audited
public class SpotProduct extends Product {

    @Column
    private LocalDate earliestReadyDate;

    @Column
    private LocalDate latestReadyDate;

    @Enumerated(EnumType.STRING)
    private LoadingType loadingType;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="LOADING_COMPANY_ID")),
            @AttributeOverride(name = "name", column=@Column(name="LOADING_COMPANY_NAME"))})
    private IdNamePair loadingCompany;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="LOADING_LOCATION_ID")),
            @AttributeOverride(name = "name", column=@Column(name="LOADING_LOCATION_NAME"))})
    private IdNamePair loadingLocation;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="DELIVERY_COMPANY_ID")),
            @AttributeOverride(name = "name", column=@Column(name="DELIVERY_COMPANY_NAME"))})
    private IdNamePair deliveryCompany;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="DELIVERY_LOCATION_ID")),
            @AttributeOverride(name = "name", column=@Column(name="DELIVERY_LOCATION_NAME"))})
    private IdNamePair deliveryLocation;

    @Column
    private Integer vehicleCount;
    
    @Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="iso", column=@Column(name="LOADING_COUNTRY_ISO")),
			@AttributeOverride(name = "name", column=@Column(name="LOADING_COUNTRY_NAME"))})
    private IsoNamePair loadingCountry;
    
	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="id", column=@Column(name="LOADING_POINT_ID")),
			@AttributeOverride(name = "name", column=@Column(name="LOADING_POINT_NAME"))})
	private IdNamePair loadingCountryPoint;
	
	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="iso", column=@Column(name="DELIVERY_COUNTRY_ISO")),
			@AttributeOverride(name = "name", column=@Column(name="DELIVERY_COUNTRY_NAME"))})
    private IsoNamePair deliveryCountry;
    
	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="id", column=@Column(name="DELIVERY_POINT_ID")),
			@AttributeOverride(name = "name", column=@Column(name="DELIVERY_POINT_NAME"))})
	private IdNamePair deliveryCountryPoint;
	
    @Column
    private Integer transitTime;
    
	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="id", column=@Column(name="TRANSSHIPMENT_PORT_ID")),
			@AttributeOverride(name = "name", column=@Column(name="TRANSSHIPMENT_PORT_NAME"))})
    private IdNamePair transshipmentPort;
	
	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="id", column=@Column(name="COLLECTION_WH_ID")),
			@AttributeOverride(name = "name", column=@Column(name="COLLECTION_WH_NAME"))})
	private IdNamePair collectionWarehouse;

	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="id", column=@Column(name="DELIVERY_WH_ID")),
			@AttributeOverride(name = "name", column=@Column(name="DELIVERY_WH_NAME"))})
	private IdNamePair deliveryWarehouse;
	
	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="code", column=@Column(name="COLLECTION_TARIFF_CODE")),
			@AttributeOverride(name = "name", column=@Column(name="COLLECTION_TARIFF_NAME"))})
	private CodeNamePair collectionTariffRegion;

	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name="code", column=@Column(name="DELIVERY_TARIFF_CODE")),
			@AttributeOverride(name = "name", column=@Column(name="DELIVERY_TARIFF_NAME"))})
	private CodeNamePair deliveryTariffRegion;


    @Override
    public ProductJson toJson(){
        SpotProductJson json = new SpotProductJson();
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
        json.setEarliestReadyDate(getEarliestReadyDate());
        json.setLatestReadyDate(getLatestReadyDate());
        json.setLoadingType(getLoadingType());
        json.setLoadingCompany(getLoadingCompany());
        json.setLoadingLocation(getLoadingLocation());
        json.setDeliveryType(getDeliveryType());
        json.setDeliveryCompany(getDeliveryCompany());
        json.setDeliveryLocation(getDeliveryLocation());
        json.setVehicleCount(getVehicleCount());
        json.setLostReason(Optional.ofNullable(getLostReason()).map(LostReasonJson::fromEntity).orElse(null));
        json.setServiceArea(BeanUtils.getBean(KartoteksService.class).findServiceAreaByCode(getServiceArea(),true));
        json.setDiscriminator(QuoteType.SPOT.name());
        json.setLoadingCountry(getLoadingCountry());
        json.setLoadingCountryPoint(getLoadingCountryPoint());
        json.setDeliveryCountry(getDeliveryCountry());
        json.setDeliveryCountryPoint(getDeliveryCountryPoint());
        json.setTransitTime(getTransitTime());
        json.setTransshipmentPort(getTransshipmentPort());
        json.setCollectionWarehouse(getCollectionWarehouse());
        json.setDeliveryWarehouse(getDeliveryWarehouse());
        json.setCollectionTariffRegion(getCollectionTariffRegion());
        json.setDeliveryTariffRegion(getDeliveryTariffRegion());
        return json;
    }
    
    @Override
    public int hashCode() {
    	return new HashCodeBuilder()
    			.appendSuper(super.hashCode())
    			.append(getEarliestReadyDate())
    			.append(getLatestReadyDate())
    			.append(getLoadingType())
    			.append(getLoadingCompany())
    			.append(getLoadingLocation())
    			.append(getDeliveryType())
    			.append(getDeliveryCompany())
    			.append(getDeliveryLocation())
    			.append(getVehicleCount())
    			.append(getLoadingCountry())
    			.append(getLoadingCountryPoint())
    			.append(getDeliveryCountry())
    			.append(getDeliveryCountryPoint())
    			.append(getCollectionWarehouse())
    			.append(getDeliveryWarehouse())
    			.append(getCollectionTariffRegion())
    			.append(getDeliveryTariffRegion())
    			.toHashCode();
    }

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SpotProduct))
			return false;
		if (object == this)
			return true;

		SpotProduct entity = SpotProduct.class.cast(object);
		return new EqualsBuilder()
				.appendSuper(super.equals(object))
				.append(entity.getEarliestReadyDate(), getEarliestReadyDate())
				.append(entity.getLatestReadyDate(), getLatestReadyDate())
				.append(entity.getLoadingType(), getLoadingType())
				.append(entity.getLoadingCompany(), getLoadingCompany())
				.append(entity.getLoadingLocation(), getLoadingLocation())
				.append(entity.getDeliveryType(), getDeliveryType())
				.append(entity.getDeliveryCompany(), getDeliveryCompany())
				.append(entity.getDeliveryLocation(), getDeliveryLocation())
				.append(entity.getVehicleCount(), getVehicleCount())
				.append(entity.getLoadingCountry(), getLoadingCountry())
				.append(entity.getLoadingCountryPoint(), getLoadingCountryPoint())
				.append(entity.getDeliveryCountry(), getDeliveryCountry())
				.append(entity.getDeliveryCountryPoint(), getDeliveryCountryPoint())
				.append(entity.getCollectionWarehouse(), getCollectionWarehouse())
    			.append(entity.getDeliveryWarehouse(), getDeliveryWarehouse())
    			.append(entity.getCollectionTariffRegion(), getCollectionTariffRegion())
    			.append(entity.getDeliveryTariffRegion(), getDeliveryTariffRegion())
				.isEquals();
	}


}
