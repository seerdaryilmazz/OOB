package ekol.orders.transportOrder.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ekol.exceptions.ApplicationException;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import ekol.orders.transportOrder.dto.VehicleFeatureFilter;

import java.io.IOException;
import java.util.*;

/**
 * Created by kilimci on 15/08/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRulesExecutedEventMessage {

    private OrderRuleResult orderResult = new OrderRuleResult();

    public OrderRuleResult getOrderResult() {
        return orderResult;
    }

    public void setOrderResult(OrderRuleResult orderResult) {
        this.orderResult = orderResult;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderRuleResult{
        private Long orderId;
        private boolean combinationAtCrossDock;
        private String freightCost;
        private Set<String> exportCustomsClearance = new HashSet<>();
        private Set<String> importCustomsClearance = new HashSet<>();
        private Set<String> collectionAt = new HashSet<>();
        private Set<String> deliveryAt = new HashSet<>();
        private Set<ApprovalResult> requiredApprovals = new HashSet<>();

        private Map<Long, ShipmentRuleResult> shipmentResults = new HashMap<>();
        private Map<Long, CustomerLocationRuleResult> customerLocationRuleResults = new HashMap<>();

        public Map<Long, ShipmentRuleResult> getShipmentResults() {
            return shipmentResults;
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public void setShipmentResults(Map<Long, ShipmentRuleResult> shipmentResults) {
            this.shipmentResults = shipmentResults;
        }

        public Set<ApprovalResult> getRequiredApprovals() {
            return requiredApprovals;
        }

        public void setRequiredApprovals(Set<ApprovalResult> requiredApprovals) {
            this.requiredApprovals = requiredApprovals;
        }

        public String getFreightCost() {
            return freightCost;
        }

        public void setFreightCost(String freightCost) {
            this.freightCost = freightCost;
        }

        public Set<String> getCollectionAt() {
            return collectionAt;
        }

        public void setCollectionAt(Set<String> collectionAt) {
            this.collectionAt = collectionAt;
        }

        public Set<String> getDeliveryAt() {
            return deliveryAt;
        }

        public void setDeliveryAt(Set<String> deliveryAt) {
            this.deliveryAt = deliveryAt;
        }

        public Set<String> getExportCustomsClearance() {
            return exportCustomsClearance;
        }

        public void setExportCustomsClearance(Set<String> exportCustomsClearance) {
            this.exportCustomsClearance = exportCustomsClearance;
        }

        public Set<String> getImportCustomsClearance() {
            return importCustomsClearance;
        }

        public void setImportCustomsClearance(Set<String> importCustomsClearance) {
            this.importCustomsClearance = importCustomsClearance;
        }

        public boolean isCombinationAtCrossDock() {
            return combinationAtCrossDock;
        }

        public void setCombinationAtCrossDock(boolean combinationAtCrossDock) {
            this.combinationAtCrossDock = combinationAtCrossDock;
        }

        public Map<Long, CustomerLocationRuleResult> getCustomerLocationRuleResults() {
            return customerLocationRuleResults;
        }

        public void setCustomerLocationRuleResults(Map<Long, CustomerLocationRuleResult> customerLocationRuleResults) {
            this.customerLocationRuleResults = customerLocationRuleResults;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShipmentRuleResult{

        private IdNamePair collectionResponsible;
        private IdNamePair linehaulResponsible;
        private IdNamePair distributionResponsible;
        private IdNamePair collectionWarehouse;
        private IdNamePair collectionRegion;
        private WarehouseRuleResult collectionWarehouseRuleResult = new WarehouseRuleResult();
        private IdNamePair distributionWarehouse;
        private IdNamePair distributionRegion;
        private WarehouseRuleResult distributionWarehouseRuleResult = new WarehouseRuleResult();

        private FixedZoneDateTime collectionArrivalShouldBeBefore;
        private FixedZoneDateTime linehaulArrivalShouldBeBefore;

        private Map<Long, ShipmentUnitRuleResult> shipmentUnitResults = new HashMap<>();

        public Map<Long, ShipmentUnitRuleResult> getShipmentUnitResults() {
            return shipmentUnitResults;
        }

        public void setShipmentUnitResults(Map<Long, ShipmentUnitRuleResult> shipmentUnitResults) {
            this.shipmentUnitResults = shipmentUnitResults;
        }

        public IdNamePair getCollectionWarehouse() {
            return collectionWarehouse;
        }

        public void setCollectionWarehouse(IdNamePair collectionWarehouse) {
            this.collectionWarehouse = collectionWarehouse;
        }

        public WarehouseRuleResult getCollectionWarehouseRuleResult() {
            return collectionWarehouseRuleResult;
        }

        public void setCollectionWarehouseRuleResult(WarehouseRuleResult collectionWarehouseRuleResult) {
            this.collectionWarehouseRuleResult = collectionWarehouseRuleResult;
        }

        public IdNamePair getDistributionWarehouse() {
            return distributionWarehouse;
        }

        public void setDistributionWarehouse(IdNamePair distributionWarehouse) {
            this.distributionWarehouse = distributionWarehouse;
        }

        public WarehouseRuleResult getDistributionWarehouseRuleResult() {
            return distributionWarehouseRuleResult;
        }

        public void setDistributionWarehouseRuleResult(WarehouseRuleResult distributionWarehouseRuleResult) {
            this.distributionWarehouseRuleResult = distributionWarehouseRuleResult;
        }

        public IdNamePair getCollectionRegion() {
            return collectionRegion;
        }

        public void setCollectionRegion(IdNamePair collectionRegion) {
            this.collectionRegion = collectionRegion;
        }

        public IdNamePair getDistributionRegion() {
            return distributionRegion;
        }

        public void setDistributionRegion(IdNamePair distributionRegion) {
            this.distributionRegion = distributionRegion;
        }

        public IdNamePair getCollectionResponsible() {
            return collectionResponsible;
        }

        public void setCollectionResponsible(IdNamePair collectionResponsible) {
            this.collectionResponsible = collectionResponsible;
        }

        public IdNamePair getLinehaulResponsible() {
            return linehaulResponsible;
        }

        public void setLinehaulResponsible(IdNamePair linehaulResponsible) {
            this.linehaulResponsible = linehaulResponsible;
        }

        public IdNamePair getDistributionResponsible() {
            return distributionResponsible;
        }

        public void setDistributionResponsible(IdNamePair distributionResponsible) {
            this.distributionResponsible = distributionResponsible;
        }

        public FixedZoneDateTime getCollectionArrivalShouldBeBefore() {
            return collectionArrivalShouldBeBefore;
        }

        public void setCollectionArrivalShouldBeBefore(FixedZoneDateTime collectionArrivalShouldBeBefore) {
            this.collectionArrivalShouldBeBefore = collectionArrivalShouldBeBefore;
        }

        public FixedZoneDateTime getLinehaulArrivalShouldBeBefore() {
            return linehaulArrivalShouldBeBefore;
        }

        public void setLinehaulArrivalShouldBeBefore(FixedZoneDateTime linehaulArrivalShouldBeBefore) {
            this.linehaulArrivalShouldBeBefore = linehaulArrivalShouldBeBefore;
        }

        public VehicleFeatureFilter mergeRequiredVehicleFeatureFilters() {
            VehicleFeatureFilter filter = new VehicleFeatureFilter();
            getShipmentUnitResults().values().stream().forEach(sur -> filter.merge(sur.getRequiredVehicleFeatures()));
            return  filter;
        }

        public VehicleFeatureFilter mergeNotAllowedVehicleFeatureFilters() {
            VehicleFeatureFilter filter = new VehicleFeatureFilter();
            getShipmentUnitResults().values().stream().forEach(sur -> filter.merge(sur.getNotAllowedVehicleFeatures()));
            return filter;
        }

        public String toJSON(){
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(this) ;
            } catch (JsonProcessingException e) {
                throw new ApplicationException("JSON writing error", e);
            }
        }

        public static ShipmentRuleResult fromJson(String json){
            if(json == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(json, ShipmentRuleResult.class) ;
            } catch (IOException e) {
                throw new ApplicationException("JSON parsing error", e);
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShipmentUnitRuleResult{

        private Integer handlingDuration;
        private boolean isLongGoods;
        private boolean isOutOfGaugeGoods;
        private boolean isHeavyGoods;
        private boolean isValuableGoods;

        private VehicleFeatureFilter requiredVehicleFeatures;
        private VehicleFeatureFilter notAllowedVehicleFeatures;

        public Integer getHandlingDuration() {
            return handlingDuration;
        }

        public void setHandlingDuration(Integer handlingDuration) {
            this.handlingDuration = handlingDuration;
        }

        public boolean isLongGoods() {
            return isLongGoods;
        }

        public void setLongGoods(boolean longGoods) {
            isLongGoods = longGoods;
        }

        public boolean isOutOfGaugeGoods() {
            return isOutOfGaugeGoods;
        }

        public void setOutOfGaugeGoods(boolean outOfGaugeGoods) {
            isOutOfGaugeGoods = outOfGaugeGoods;
        }

        public boolean isHeavyGoods() {
            return isHeavyGoods;
        }

        public void setHeavyGoods(boolean heavyGoods) {
            isHeavyGoods = heavyGoods;
        }

        public boolean isValuableGoods() {
            return isValuableGoods;
        }

        public void setValuableGoods(boolean valuableGoods) {
            isValuableGoods = valuableGoods;
        }

        public VehicleFeatureFilter getRequiredVehicleFeatures() {
            return requiredVehicleFeatures;
        }

        public void setRequiredVehicleFeatures(VehicleFeatureFilter requiredVehicleFeatures) {
            this.requiredVehicleFeatures = requiredVehicleFeatures;
        }

        public VehicleFeatureFilter getNotAllowedVehicleFeatures() {
            return notAllowedVehicleFeatures;
        }

        public void setNotAllowedVehicleFeatures(VehicleFeatureFilter notAllowedVehicleFeatures) {
            this.notAllowedVehicleFeatures = notAllowedVehicleFeatures;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WarehouseRuleResult{
        private boolean useRfWhenLoading;
        private boolean useRfWhenUnloading;
        private Set<String> handlingStaff = new HashSet<>();
        private Set<String> specialEquipment = new HashSet<>();

        public boolean isUseRfWhenLoading() {
            return useRfWhenLoading;
        }

        public void setUseRfWhenLoading(boolean useRfWhenLoading) {
            this.useRfWhenLoading = useRfWhenLoading;
        }

        public boolean isUseRfWhenUnloading() {
            return useRfWhenUnloading;
        }

        public void setUseRfWhenUnloading(boolean useRfWhenUnloading) {
            this.useRfWhenUnloading = useRfWhenUnloading;
        }

        public Set<String> getHandlingStaff() {
            return handlingStaff;
        }

        public void setHandlingStaff(Set<String> handlingStaff) {
            this.handlingStaff = handlingStaff;
        }

        public Set<String> getSpecialEquipment() {
            return specialEquipment;
        }

        public void setSpecialEquipment(Set<String> specialEquipment) {
            this.specialEquipment = specialEquipment;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerLocationRuleResult {
        private List<IdNamePair> truckBrandsRequired;
        private List<IdNamePair> truckBrandsNotAllowed;

        private boolean rentedTrucksProhibited;
        private boolean rentedTrailersProhibited;

        private IdNamePair driverProfile;
        private boolean driverAccompanyNotAllowed;

        private List<RequiredEquipmentResult> requiredEquipmentResults = new ArrayList<>();

        public List<IdNamePair> getTruckBrandsRequired() {
            return truckBrandsRequired;
        }

        public void setTruckBrandsRequired(List<IdNamePair> truckBrandsRequired) {
            this.truckBrandsRequired = truckBrandsRequired;
        }

        public List<IdNamePair> getTruckBrandsNotAllowed() {
            return truckBrandsNotAllowed;
        }

        public void setTruckBrandsNotAllowed(List<IdNamePair> truckBrandsNotAllowed) {
            this.truckBrandsNotAllowed = truckBrandsNotAllowed;
        }

        public boolean isRentedTrucksProhibited() {
            return rentedTrucksProhibited;
        }

        public void setRentedTrucksProhibited(boolean rentedTrucksProhibited) {
            this.rentedTrucksProhibited = rentedTrucksProhibited;
        }

        public boolean isRentedTrailersProhibited() {
            return rentedTrailersProhibited;
        }

        public void setRentedTrailersProhibited(boolean rentedTrailersProhibited) {
            this.rentedTrailersProhibited = rentedTrailersProhibited;
        }

        public IdNamePair getDriverProfile() {
            return driverProfile;
        }

        public void setDriverProfile(IdNamePair driverProfile) {
            this.driverProfile = driverProfile;
        }

        public boolean isDriverAccompanyNotAllowed() {
            return driverAccompanyNotAllowed;
        }

        public void setDriverAccompanyNotAllowed(boolean driverAccompanyNotAllowed) {
            this.driverAccompanyNotAllowed = driverAccompanyNotAllowed;
        }

        public List<RequiredEquipmentResult> getRequiredEquipmentResults() {
            return requiredEquipmentResults;
        }

        public void setRequiredEquipmentResults(List<RequiredEquipmentResult> requiredEquipmentResults) {
            this.requiredEquipmentResults = requiredEquipmentResults;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApprovalResult{
        private String level;
        private String reason;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }


    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequiredEquipmentResult {
        private IdNamePair equipment;
        private Integer amount;

        public IdNamePair getEquipment() {
            return equipment;
        }

        public void setEquipment(IdNamePair equipment) {
            this.equipment = equipment;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }

}
