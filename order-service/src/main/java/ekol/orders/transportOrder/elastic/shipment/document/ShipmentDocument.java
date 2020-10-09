package ekol.orders.transportOrder.elastic.shipment.document;

import ekol.orders.lookup.domain.PermissionType;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import ekol.orders.transportOrder.domain.Shipment;
import ekol.orders.transportOrder.elastic.shipment.model.ShipmentSize;
import ekol.orders.transportOrder.event.OrderRulesExecutedEventMessage;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ozer on 03/10/16.
 */
@Document(indexName = ShipmentDocument.INDEX_NAME)
@Setting(settingPath = "/elastic/ShipmentSettings.json")
public class ShipmentDocument {

    public static final String INDEX_NAME = "shipment";

    private String id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String code;

    @MultiField(
            mainField = @Field(type = FieldType.String, index = FieldIndex.not_analyzed),
            otherFields = {
                    @InnerField(type = FieldType.String, indexAnalyzer = "turkish", searchAnalyzer = "turkish", suffix = "tr")
            }
    )
    private String customerName;

    @Field(type = FieldType.Long)
    private Long transportOrderId;

    @Field(type = FieldType.Nested)
    private IdNamePair subsidiary;

    @Field(type = FieldType.Nested)
    private SenderOrReceiverDocument sender;

    @Field(type = FieldType.Nested)
    private SenderOrReceiverDocument receiver;

    @Field(type = FieldType.Nested)
    private DateTimeDocument readyAtDate;

    @Field(type = FieldType.Nested)
    private DateTimeDocument requestedDeliveryDate;

    @Field(type = FieldType.Nested)
    private DateTimeDocument collectionArrivalDate;

    @Field(type = FieldType.Nested)
    private DateTimeDocument linehaulArrivalDate;

    @Field(type = FieldType.Nested)
    private DateTimeWindowDocument pickupAppointment;

    @Field(type = FieldType.Nested)
    private DateTimeWindowDocument deliveryAppointment;

    @Field(type = FieldType.Nested)
    private WarehouseDocument collectionWarehouse;

    @Field(type = FieldType.Nested)
    private WarehouseDocument distributionWarehouse;

    @Field(type = FieldType.Double)
    private Double grossWeight;

    @Field(type = FieldType.Double)
    private Double volume;

    @Field(type = FieldType.Double)
    private Double ldm;

    @Field(type = FieldType.Double)
    private Double payWeight;

    @Field(type = FieldType.Integer)
    private Integer packageCount;

    @Field(type = FieldType.Nested)
    private List<PackageTypeDocument> packageTypes;

    // Grouping does not return parent document. Because of this, we index nested grouping fields seperately
    // These fields are: senderPostalCode, receiverPostalCode, senderCompanyName, receiverCompanyName, transferWarehouseName

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String senderPostalCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String receiverPostalCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String senderCompanyName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String receiverCompanyName;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String truckLoadType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String serviceType;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String status;

    @Field(type = FieldType.Nested)
    private List<ShipmentUnitDocument> units = new ArrayList<>();

    @Field(type = FieldType.Nested)
    private VehicleFeatureFilterDocument requiredVehicleFeaturesFromOrder;
    @Field(type = FieldType.Nested)
    private VehicleFeatureFilterDocument notAllowedVehicleFeaturesFromOrder;

    @Field(type = FieldType.Nested)
    private VehicleFeatureFilterDocument requiredVehicleFeaturesFromRule;
    @Field(type = FieldType.Nested)
    private VehicleFeatureFilterDocument notAllowedVehicleFeaturesFromRule;

    @Field(type = FieldType.Nested)
    private VehicleFeatureFilterDocument requiredVehicleFeatures;
    @Field(type = FieldType.Nested)
    private VehicleFeatureFilterDocument notAllowedVehicleFeatures;

    public static ShipmentDocument fromShipment(Shipment shipment){
        ShipmentDocument document = new ShipmentDocument();
        ShipmentSize size = shipment.getSize();
        document.setId(String.valueOf(shipment.getId()));
        document.setCode(shipment.getCode());
        document.setSubsidiary(shipment.getTransportOrder().getSubsidiary());
        document.setCustomerName(shipment.getTransportOrder().getCustomer().getName());
        document.setTransportOrderId(shipment.getTransportOrder().getId());
        document.setSender(SenderOrReceiverDocument.fromSenderOrReceiver(shipment.getSender()));
        document.setReceiver(SenderOrReceiverDocument.fromSenderOrReceiver(shipment.getReceiver()));
        document.setReadyAtDate(DateTimeDocument.withFixedZoneDateTime(shipment.getReadyAtDate()));
        document.setRequestedDeliveryDate(DateTimeDocument.withFixedZoneDateTime(shipment.getRequestedDeliveryDate()));
        document.setCollectionArrivalDate(DateTimeDocument.withFixedZoneDateTime(shipment.getCollectionArrivalDate()));
        document.setLinehaulArrivalDate(DateTimeDocument.withFixedZoneDateTime(shipment.getLinehaulArrivalDate()));

        document.setTruckLoadType(shipment.getTransportOrder().getTruckLoadType().name());
        document.setServiceType(shipment.getTransportOrder().getServiceType().name());
        document.setStatus(shipment.getStatus().name());
        document.setPickupAppointment(DateTimeWindowDocument.fromFixedZoneDateTimeWindow(shipment.getPickupAppointment()));
        document.setDeliveryAppointment(DateTimeWindowDocument.fromFixedZoneDateTimeWindow(shipment.getDeliveryAppointment()));
        document.setGrossWeight(size.getGrossWeight().doubleValue());
        document.setVolume(size.getVolume().doubleValue());
        document.setLdm(size.getLdm().doubleValue());
        document.setPayWeight(shipment.getPayWeight().doubleValue());
        document.setPackageCount(size.getCount());
        document.setPackageTypes(createPackageTypeDocuments(shipment));

        // Grouping does not return parent document. Because of this, we index nested grouping fields seperately
        // These fields are: senderPostalCode, receiverPostalCode, senderCompanyName, receiverCompanyName, transferWarehouseName
        document.setSenderPostalCode(document.getSender() != null && document.getSender().getLocation() != null ? document.getSender().getLocation().getPostalCode() : null);
        document.setReceiverPostalCode(document.getReceiver() != null && document.getReceiver().getLocation() != null ? document.getReceiver().getLocation().getPostalCode() : null);
        document.setSenderCompanyName(document.getSender() != null ? document.getSender().getCompanyName() : null);
        document.setReceiverCompanyName(document.getReceiver() != null ? document.getReceiver().getCompanyName() : null);

        if(shipment.getCollectionWarehouse() != null){
            document.setCollectionWarehouse(
                    WarehouseDocument.createWith(shipment.getCollectionWarehouse(), shipment.getCollectionWarehouseLocation()));
        }
        if(shipment.getDistributionWarehouse() != null) {
            document.setDistributionWarehouse(
                    WarehouseDocument.createWith(shipment.getDistributionWarehouse(), shipment.getDistributionWarehouseLocation()));
        }
        shipment.getShipmentUnits().forEach(shipmentUnit -> {
            document.getUnits().add(ShipmentUnitDocument.createWith(shipmentUnit));
        });


        //transport order may have ultiple vehicle feature fitlers; merge all of them
        document.setRequiredVehicleFeaturesFromOrder(new VehicleFeatureFilterDocument());
        document.setNotAllowedVehicleFeaturesFromOrder(new VehicleFeatureFilterDocument());
        shipment.getTransportOrder().getVehicleRequirements().stream().forEach(requirement -> {
            VehicleFeatureFilterDocument vehicleFeatureFilterDocument = VehicleFeatureFilterDocument.fromTransportOrderRequirement(requirement);
            if(requirement.getPermissionType() == PermissionType.REQUIRED) {
                document.getRequiredVehicleFeaturesFromOrder().merge(vehicleFeatureFilterDocument);
            }else if(requirement.getPermissionType() == PermissionType.NOT_ALLOWED) {
                document.getNotAllowedVehicleFeaturesFromOrder().merge(vehicleFeatureFilterDocument);
            }
        });

        //set vehicle feature filters coming from rules
        OrderRulesExecutedEventMessage.ShipmentRuleResult shipmentRuleResult = OrderRulesExecutedEventMessage.ShipmentRuleResult.fromJson(shipment.getExecutedRuleResult());
        if(shipmentRuleResult != null) {
            document.setRequiredVehicleFeaturesFromRule(
                    VehicleFeatureFilterDocument.fromVehicleFeatureFilter(shipmentRuleResult.mergeRequiredVehicleFeatureFilters())
            );
            document.setNotAllowedVehicleFeaturesFromRule(
                    VehicleFeatureFilterDocument.fromVehicleFeatureFilter(shipmentRuleResult.mergeNotAllowedVehicleFeatureFilters())
            );
        }

        //merge vehicle features comnig from order and rules
        document.setRequiredVehicleFeatures(document.getRequiredVehicleFeaturesFromOrder());
        document.getRequiredVehicleFeatures().merge(document.getRequiredVehicleFeaturesFromRule());
        document.setNotAllowedVehicleFeatures(document.getNotAllowedVehicleFeaturesFromOrder());
        document.getNotAllowedVehicleFeatures().merge(document.getNotAllowedVehicleFeaturesFromRule());

        return document;
    }

    private static List<PackageTypeDocument> createPackageTypeDocuments(Shipment shipment) {
        Map<String, Integer> packageTypes = shipment.getPackageTypesAndCounts();
        List<PackageTypeDocument> packageTypeDocuments = new ArrayList<>();
        packageTypes.entrySet().forEach(entry -> packageTypeDocuments.add(new PackageTypeDocument(entry.getKey(), entry.getValue())));
        return packageTypeDocuments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public IdNamePair getSubsidiary() {
        return subsidiary;
    }

    public void setSubsidiary(IdNamePair subsidiary) {
        this.subsidiary = subsidiary;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getTransportOrderId() {
        return transportOrderId;
    }

    public void setTransportOrderId(Long transportOrderId) {
        this.transportOrderId = transportOrderId;
    }

    public SenderOrReceiverDocument getSender() {
        return sender;
    }

    public void setSender(SenderOrReceiverDocument sender) {
        this.sender = sender;
    }

    public SenderOrReceiverDocument getReceiver() {
        return receiver;
    }

    public void setReceiver(SenderOrReceiverDocument receiver) {
        this.receiver = receiver;
    }

    public DateTimeDocument getReadyAtDate() {
        return readyAtDate;
    }

    public void setReadyAtDate(DateTimeDocument readyAtDate) {
        this.readyAtDate = readyAtDate;
    }

    public DateTimeDocument getRequestedDeliveryDate() {
        return requestedDeliveryDate;
    }

    public void setRequestedDeliveryDate(DateTimeDocument requestedDeliveryDate) {
        this.requestedDeliveryDate = requestedDeliveryDate;
    }

    public DateTimeDocument getCollectionArrivalDate() {
        return collectionArrivalDate;
    }

    public void setCollectionArrivalDate(DateTimeDocument collectionArrivalDate) {
        this.collectionArrivalDate = collectionArrivalDate;
    }

    public DateTimeDocument getLinehaulArrivalDate() {
        return linehaulArrivalDate;
    }

    public void setLinehaulArrivalDate(DateTimeDocument linehaulArrivalDate) {
        this.linehaulArrivalDate = linehaulArrivalDate;
    }

    public DateTimeWindowDocument getPickupAppointment() {
        return pickupAppointment;
    }

    public void setPickupAppointment(DateTimeWindowDocument pickupAppointment) {
        this.pickupAppointment = pickupAppointment;
    }

    public DateTimeWindowDocument getDeliveryAppointment() {
        return deliveryAppointment;
    }

    public void setDeliveryAppointment(DateTimeWindowDocument deliveryAppointment) {
        this.deliveryAppointment = deliveryAppointment;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getLdm() {
        return ldm;
    }

    public void setLdm(Double ldm) {
        this.ldm = ldm;
    }

    public Double getPayWeight() {
        return payWeight;
    }

    public void setPayWeight(Double payWeight) {
        this.payWeight = payWeight;
    }

    public Integer getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(Integer packageCount) {
        this.packageCount = packageCount;
    }

    public List<PackageTypeDocument> getPackageTypes() {
        return packageTypes;
    }

    public void setPackageTypes(List<PackageTypeDocument> packageTypes) {
        this.packageTypes = packageTypes;
    }

    public String getSenderPostalCode() {
        return senderPostalCode;
    }

    public void setSenderPostalCode(String senderPostalCode) {
        this.senderPostalCode = senderPostalCode;
    }

    public String getReceiverPostalCode() {
        return receiverPostalCode;
    }

    public void setReceiverPostalCode(String receiverPostalCode) {
        this.receiverPostalCode = receiverPostalCode;
    }

    public String getSenderCompanyName() {
        return senderCompanyName;
    }

    public void setSenderCompanyName(String senderCompanyName) {
        this.senderCompanyName = senderCompanyName;
    }

    public String getReceiverCompanyName() {
        return receiverCompanyName;
    }

    public void setReceiverCompanyName(String receiverCompanyName) {
        this.receiverCompanyName = receiverCompanyName;
    }

    public String getTruckLoadType() {
        return truckLoadType;
    }

    public void setTruckLoadType(String truckLoadType) {
        this.truckLoadType = truckLoadType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ShipmentUnitDocument> getUnits() {
        return units;
    }

    public void setUnits(List<ShipmentUnitDocument> units) {
        this.units = units;
    }

    public WarehouseDocument getCollectionWarehouse() {
        return collectionWarehouse;
    }

    public void setCollectionWarehouse(WarehouseDocument collectionWarehouse) {
        this.collectionWarehouse = collectionWarehouse;
    }

    public WarehouseDocument getDistributionWarehouse() {
        return distributionWarehouse;
    }

    public void setDistributionWarehouse(WarehouseDocument distributionWarehouse) {
        this.distributionWarehouse = distributionWarehouse;
    }

    public VehicleFeatureFilterDocument getRequiredVehicleFeaturesFromOrder() {
        return requiredVehicleFeaturesFromOrder;
    }

    public void setRequiredVehicleFeaturesFromOrder(VehicleFeatureFilterDocument requiredVehicleFeaturesFromOrder) {
        this.requiredVehicleFeaturesFromOrder = requiredVehicleFeaturesFromOrder;
    }

    public VehicleFeatureFilterDocument getNotAllowedVehicleFeaturesFromOrder() {
        return notAllowedVehicleFeaturesFromOrder;
    }

    public void setNotAllowedVehicleFeaturesFromOrder(VehicleFeatureFilterDocument notAllowedVehicleFeaturesFromOrder) {
        this.notAllowedVehicleFeaturesFromOrder = notAllowedVehicleFeaturesFromOrder;
    }

    public VehicleFeatureFilterDocument getRequiredVehicleFeaturesFromRule() {
        return requiredVehicleFeaturesFromRule;
    }

    public void setRequiredVehicleFeaturesFromRule(VehicleFeatureFilterDocument requiredVehicleFeaturesFromRule) {
        this.requiredVehicleFeaturesFromRule = requiredVehicleFeaturesFromRule;
    }

    public VehicleFeatureFilterDocument getNotAllowedVehicleFeaturesFromRule() {
        return notAllowedVehicleFeaturesFromRule;
    }

    public void setNotAllowedVehicleFeaturesFromRule(VehicleFeatureFilterDocument notAllowedVehicleFeaturesFromRule) {
        this.notAllowedVehicleFeaturesFromRule = notAllowedVehicleFeaturesFromRule;
    }

    public VehicleFeatureFilterDocument getRequiredVehicleFeatures() {
        return requiredVehicleFeatures;
    }

    public void setRequiredVehicleFeatures(VehicleFeatureFilterDocument requiredVehicleFeatures) {
        this.requiredVehicleFeatures = requiredVehicleFeatures;
    }

    public VehicleFeatureFilterDocument getNotAllowedVehicleFeatures() {
        return notAllowedVehicleFeatures;
    }

    public void setNotAllowedVehicleFeatures(VehicleFeatureFilterDocument notAllowedVehicleFeatures) {
        this.notAllowedVehicleFeatures = notAllowedVehicleFeatures;
    }
}
