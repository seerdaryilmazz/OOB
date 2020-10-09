package ekol.crm.quote.domain.dto.product;

import java.time.LocalDate;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.common.Constants;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.product.*;
import ekol.exceptions.ValidationException;
import ekol.model.*;
import lombok.*;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotProductJson extends ProductJson {

    private LocalDate earliestReadyDate;
    private LocalDate latestReadyDate;
    private LoadingType loadingType;
    private IdNamePair loadingCompany;
    private IdNamePair loadingLocation;
    private DeliveryType deliveryType;
    private IdNamePair deliveryCompany;
    private IdNamePair deliveryLocation;
    private Integer vehicleCount;
    private IsoNamePair loadingCountry;
    private IdNamePair loadingCountryPoint;
    private IsoNamePair deliveryCountry;
    private IdNamePair deliveryCountryPoint;
    private Integer transitTime;
    private IdNamePair transshipmentPort;
    private IdNamePair collectionWarehouse;
    private IdNamePair deliveryWarehouse;
    private CodeNamePair collectionTariffRegion;
    private CodeNamePair deliveryTariffRegion;

    @Override
    public Product toEntity(){
        SpotProduct entity = new SpotProduct();
        entity.setId(getId());
        entity.setFromCountry(getFromCountry());
        entity.setFromPoint(getFromPoint());
        entity.setToCountry(getToCountry());
        entity.setToPoint(getToPoint());
        entity.setShipmentLoadingType(getShipmentLoadingType());
        entity.setIncoterm(getIncoterm());
        entity.setIncotermExplanation(getIncotermExplanation());
        entity.setCalculationType(getCalculationType());
        entity.setStatus(getStatus());
        entity.setEarliestReadyDate(getEarliestReadyDate());
        entity.setLatestReadyDate(getLatestReadyDate());
        entity.setLoadingType(getLoadingType());
        entity.setLoadingCompany(getLoadingCompany());
        entity.setLoadingLocation(getLoadingLocation());
        entity.setDeliveryType(getDeliveryType());
        entity.setDeliveryCompany(getDeliveryCompany());
        entity.setDeliveryLocation(getDeliveryLocation());
        entity.setVehicleCount(getVehicleCount());
        entity.setLostReason(getLostReason() != null ? getLostReason().toEntity() : null);
        entity.setServiceArea(getServiceArea().getCode());
        entity.setLoadingCountry(getLoadingCountry());
        entity.setLoadingCountryPoint(getLoadingCountryPoint());
        entity.setDeliveryCountry(getDeliveryCountry());
        entity.setDeliveryCountryPoint(getDeliveryCountryPoint());
        entity.setTransitTime(getTransitTime());
        entity.setTransshipmentPort(getTransshipmentPort());
        entity.setCollectionWarehouse(getCollectionWarehouse());
        entity.setDeliveryWarehouse(getDeliveryWarehouse());
        entity.setCollectionTariffRegion(getCollectionTariffRegion());
        entity.setDeliveryTariffRegion(getDeliveryTariffRegion());
        return entity;
    }

    @Override
    public void validate(QuoteType type) {

        if(getEarliestReadyDate() == null){
            throw new ValidationException("Earliest ready date should not be empty");
        }
        if(getLatestReadyDate() == null){
            throw new ValidationException("Latest ready date should not be empty");
        }

        if(getServiceArea().getCode().equals("ROAD")){

            if(StringUtils.isEmpty(getIncoterm())){
                throw new ValidationException("Incoterm should not be empty");
            }

            if(StringUtils.isEmpty(getShipmentLoadingType())){
                throw new ValidationException("Shipment loading type should not be empty");
            }
            if(getLoadingType() == null){
                throw new ValidationException("Loading type should not be empty");
            }
            if(getDeliveryType() == null){
                throw new ValidationException("Delivery type should not be empty");
            }
            if(!StringUtils.isEmpty(getLoadingType().getSource())){
                if(getLoadingLocation() == null || getLoadingLocation().getId() == null){
                    throw new ValidationException("Loading Address should not be empty");
                }
            }

            if(!StringUtils.isEmpty(getDeliveryType().getSource())){
                if(getDeliveryLocation() == null || getDeliveryLocation().getId() == null){
                    throw new ValidationException("Delivery Address should not be empty");
                }
            }
            if(Constants.SHIPMENT_LOADING_TYPE_FTL.equals(getShipmentLoadingType())){
                if(getVehicleCount() == null || getVehicleCount() == 0){
                    throw new ValidationException("Vehicle count should not be empty for FTL product");
                }
            }
        }else if(getServiceArea().getCode().equals("SEA")){

            if(StringUtils.isEmpty(getIncoterm())){
                throw new ValidationException("Incoterm should not be empty");
            }
            if(StringUtils.isEmpty(getShipmentLoadingType())){
                throw new ValidationException("Shipment loading type should not be empty");
            }
            if(StringUtils.isEmpty(getIncotermExplanation())){
                throw new ValidationException("Incoterm explanation should not be empty");
            }
        }else if(getServiceArea().getCode().equals("AIR")){

            if(StringUtils.isEmpty(getIncoterm())){
                throw new ValidationException("Incoterm should not be empty");
            }
            if(StringUtils.isEmpty(getIncotermExplanation())){
                throw new ValidationException("Incoterm explanation should not be empty");
            }
        }else if(getServiceArea().getCode().equals("DTR")){

            if(StringUtils.isEmpty(getShipmentLoadingType())){
                throw new ValidationException("Shipment loading type should not be empty");
            }
        }
    }



}
