package ekol.orders.transportOrder.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTimeWindow;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import ekol.orders.transportOrder.domain.TransportOrder;
import ekol.orders.transportOrder.domain.TransportOrderRequest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kilimci on 09/08/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCreatedEventMessage {
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public static OrderCreatedEventMessage createWith(TransportOrder transportOrder){
        OrderCreatedEventMessage message = new OrderCreatedEventMessage();
        message.setOrder(Order.createWith(transportOrder));
        return message;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {
        private Long id;
        private OrderRequest request;
        private IdNamePair subsidiary;
        private Long customerId;
        private String serviceType;
        private String truckLoadType;
        private String incoterm;
        private String status;
        private FixedZoneDateTime readyAtDate;
        private Set<Shipment> shipments = new HashSet<>();

        public static Order createWith(TransportOrder transportOrder){
            Order order = new Order();
            order.setId(transportOrder.getId());
            order.setRequest(OrderRequest.createWith(transportOrder.getRequest()));
            order.setSubsidiary(transportOrder.getSubsidiary());
            order.setCustomerId(transportOrder.getCustomerId());
            order.setServiceType(transportOrder.getServiceType().name());
            order.setTruckLoadType(transportOrder.getTruckLoadType().name());
            order.setIncoterm(transportOrder.getIncoterm().getCode());
            order.setStatus(transportOrder.getStatus().name());
            order.setReadyAtDate(transportOrder.getReadyAtDate());
            transportOrder.getShipments().forEach(shipment -> {
                order.getShipments().add(Shipment.createWith(shipment));
            });
            return order;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public OrderRequest getRequest() {
            return request;
        }

        public void setRequest(OrderRequest request) {
            this.request = request;
        }

        public IdNamePair getSubsidiary() {
            return subsidiary;
        }

        public void setSubsidiary(IdNamePair subsidiary) {
            this.subsidiary = subsidiary;
        }

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public String getTruckLoadType() {
            return truckLoadType;
        }

        public void setTruckLoadType(String truckLoadType) {
            this.truckLoadType = truckLoadType;
        }

        public String getIncoterm() {
            return incoterm;
        }

        public void setIncoterm(String incoterm) {
            this.incoterm = incoterm;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public FixedZoneDateTime getReadyAtDate() {
            return readyAtDate;
        }

        public void setReadyAtDate(FixedZoneDateTime readyAtDate) {
            this.readyAtDate = readyAtDate;
        }

        public Set<Shipment> getShipments() {
            return shipments;
        }

        public void setShipments(Set<Shipment> shipments) {
            this.shipments = shipments;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderRequest {

        private Long id;
        private IdNamePair contract;
        private String orderType;

        public static OrderRequest createWith(TransportOrderRequest transportOrderRequest) {

            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setId(transportOrderRequest.getId());
            if (transportOrderRequest.getContract() != null) {
                orderRequest.setContract(new IdNamePair(transportOrderRequest.getContract().getId(), transportOrderRequest.getContract().getName()));
            }
            if (transportOrderRequest.getOrderType() != null) {
                orderRequest.setOrderType(transportOrderRequest.getOrderType().name());
            }

            return orderRequest;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public IdNamePair getContract() {
            return contract;
        }

        public void setContract(IdNamePair contract) {
            this.contract = contract;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Shipment {
        private Long id;
        private String code;
        private String adrClass;
        private IdNamePair sender;
        private IdNamePair senderLocation;
        private IdNamePair senderContact;
        private Long senderLocationRegionId;
        private String senderLocationRegionCategoryId;
        private Long senderLocationOperationRegionId;
        private String senderLocationTimezone;
        private IdNamePair receiver;
        private IdNamePair receiverLocation;
        private IdNamePair receiverContact;
        private Long receiverLocationRegionId;
        private String receiverLocationRegionCategoryId;
        private Long receiverLocationOperationRegionId;
        private String receiverLocationTimezone;
        private IdNamePair collectionWarehouse;
        private IdNamePair distributionWarehouse;
        private FixedZoneDateTime readyAtDate;
        private FixedZoneDateTime requestedDeliveryDate;
        private FixedZoneDateTime collectionArrivalDate;
        private FixedZoneDateTime linehaulArrivalDate;

        private FixedZoneDateTimeWindow pickupAppointment;
        private FixedZoneDateTimeWindow deliveryAppointment;
        private Set<ShipmentUnit> shipmentUnits = new HashSet<>();
        private BigDecimal payWeight;
        private String planningStatus;
        private String shipmentStatus;

        public static Shipment createWith(ekol.orders.transportOrder.domain.Shipment orderShipment){
            Shipment shipment = new Shipment();
            shipment.setId(orderShipment.getId());
            shipment.setCode(orderShipment.getCode());
            shipment.setAdrClass(orderShipment.getAdrClass() != null ? orderShipment.getAdrClass().getCode() : "");

            shipment.setSender(orderShipment.getSender().getCompany().asIdNamePair());
            shipment.setSenderLocation(orderShipment.getSender().getLocation().asIdNamePair());
            shipment.setSenderContact(orderShipment.getSender().getCompanyContact().asIdNamePair());
            shipment.setSenderLocationRegionId(orderShipment.getSender().getLocationRegionId());
            shipment.setSenderLocationRegionCategoryId(orderShipment.getSender().getLocationRegionCategoryId());
            shipment.setSenderLocationOperationRegionId(orderShipment.getSender().getLocationOperationRegionId());
            shipment.setSenderLocationTimezone(orderShipment.getSender().getLocation().getTimezone());

            shipment.setReceiver(orderShipment.getReceiver().getCompany().asIdNamePair());
            shipment.setReceiverLocation(orderShipment.getReceiver().getLocation().asIdNamePair());
            shipment.setReceiverContact(orderShipment.getReceiver().getCompanyContact().asIdNamePair());
            shipment.setReceiverLocationRegionId(orderShipment.getReceiver().getLocationRegionId());
            shipment.setReceiverLocationRegionCategoryId(orderShipment.getReceiver().getLocationRegionCategoryId());
            shipment.setReceiverLocationOperationRegionId(orderShipment.getReceiver().getLocationOperationRegionId());
            shipment.setReceiverLocationTimezone(orderShipment.getReceiver().getLocation().getTimezone());

            shipment.setCollectionWarehouse(orderShipment.getCollectionWarehouse());
            shipment.setDistributionWarehouse(orderShipment.getDistributionWarehouse());

            shipment.setReadyAtDate(orderShipment.getReadyAtDate());
            shipment.setRequestedDeliveryDate(orderShipment.getRequestedDeliveryDate());
            shipment.setCollectionArrivalDate(orderShipment.getCollectionArrivalDate());
            shipment.setLinehaulArrivalDate(orderShipment.getLinehaulArrivalDate());

            shipment.setPickupAppointment(orderShipment.getPickupAppointment());
            shipment.setDeliveryAppointment(orderShipment.getDeliveryAppointment());

            shipment.setPayWeight(orderShipment.getPayWeight());

            orderShipment.getShipmentUnits().forEach(shipmentUnit -> {
                shipment.getShipmentUnits().add(ShipmentUnit.createWith(shipmentUnit));
            });
            return shipment;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getAdrClass() {
            return adrClass;
        }

        public void setAdrClass(String adrClass) {
            this.adrClass = adrClass;
        }

        public IdNamePair getSender() {
            return sender;
        }

        public void setSender(IdNamePair sender) {
            this.sender = sender;
        }

        public IdNamePair getSenderLocation() {
            return senderLocation;
        }

        public void setSenderLocation(IdNamePair senderLocation) {
            this.senderLocation = senderLocation;
        }

        public IdNamePair getSenderContact() {
            return senderContact;
        }

        public void setSenderContact(IdNamePair senderContact) {
            this.senderContact = senderContact;
        }

        public Long getSenderLocationRegionId() {
            return senderLocationRegionId;
        }

        public void setSenderLocationRegionId(Long senderLocationRegionId) {
            this.senderLocationRegionId = senderLocationRegionId;
        }

        public String getSenderLocationRegionCategoryId() {
            return senderLocationRegionCategoryId;
        }

        public void setSenderLocationRegionCategoryId(String senderLocationRegionCategoryId) {
            this.senderLocationRegionCategoryId = senderLocationRegionCategoryId;
        }

        public Long getSenderLocationOperationRegionId() {
            return senderLocationOperationRegionId;
        }

        public void setSenderLocationOperationRegionId(Long senderLocationOperationRegionId) {
            this.senderLocationOperationRegionId = senderLocationOperationRegionId;
        }

        public String getSenderLocationTimezone() {
            return senderLocationTimezone;
        }

        public void setSenderLocationTimezone(String senderLocationTimezone) {
            this.senderLocationTimezone = senderLocationTimezone;
        }

        public IdNamePair getReceiver() {
            return receiver;
        }

        public void setReceiver(IdNamePair receiver) {
            this.receiver = receiver;
        }

        public IdNamePair getReceiverLocation() {
            return receiverLocation;
        }

        public void setReceiverLocation(IdNamePair receiverLocation) {
            this.receiverLocation = receiverLocation;
        }

        public IdNamePair getReceiverContact() {
            return receiverContact;
        }

        public void setReceiverContact(IdNamePair receiverContact) {
            this.receiverContact = receiverContact;
        }

        public Long getReceiverLocationRegionId() {
            return receiverLocationRegionId;
        }

        public void setReceiverLocationRegionId(Long receiverLocationRegionId) {
            this.receiverLocationRegionId = receiverLocationRegionId;
        }

        public String getReceiverLocationRegionCategoryId() {
            return receiverLocationRegionCategoryId;
        }

        public void setReceiverLocationRegionCategoryId(String receiverLocationRegionCategoryId) {
            this.receiverLocationRegionCategoryId = receiverLocationRegionCategoryId;
        }

        public Long getReceiverLocationOperationRegionId() {
            return receiverLocationOperationRegionId;
        }

        public void setReceiverLocationOperationRegionId(Long receiverLocationOperationRegionId) {
            this.receiverLocationOperationRegionId = receiverLocationOperationRegionId;
        }

        public String getReceiverLocationTimezone() {
            return receiverLocationTimezone;
        }

        public void setReceiverLocationTimezone(String receiverLocationTimezone) {
            this.receiverLocationTimezone = receiverLocationTimezone;
        }

        public IdNamePair getCollectionWarehouse() {
            return collectionWarehouse;
        }

        public void setCollectionWarehouse(IdNamePair collectionWarehouse) {
            this.collectionWarehouse = collectionWarehouse;
        }

        public IdNamePair getDistributionWarehouse() {
            return distributionWarehouse;
        }

        public void setDistributionWarehouse(IdNamePair distributionWarehouse) {
            this.distributionWarehouse = distributionWarehouse;
        }

        public FixedZoneDateTime getReadyAtDate() {
            return readyAtDate;
        }

        public void setReadyAtDate(FixedZoneDateTime readyAtDate) {
            this.readyAtDate = readyAtDate;
        }

        public FixedZoneDateTime getRequestedDeliveryDate() {
            return requestedDeliveryDate;
        }

        public void setRequestedDeliveryDate(FixedZoneDateTime requestedDeliveryDate) {
            this.requestedDeliveryDate = requestedDeliveryDate;
        }

        public FixedZoneDateTime getCollectionArrivalDate() {
            return collectionArrivalDate;
        }

        public void setCollectionArrivalDate(FixedZoneDateTime collectionArrivalDate) {
            this.collectionArrivalDate = collectionArrivalDate;
        }

        public FixedZoneDateTime getLinehaulArrivalDate() {
            return linehaulArrivalDate;
        }

        public void setLinehaulArrivalDate(FixedZoneDateTime linehaulArrivalDate) {
            this.linehaulArrivalDate = linehaulArrivalDate;
        }

        public FixedZoneDateTimeWindow getPickupAppointment() {
            return pickupAppointment;
        }

        public void setPickupAppointment(FixedZoneDateTimeWindow pickupAppointment) {
            this.pickupAppointment = pickupAppointment;
        }

        public FixedZoneDateTimeWindow getDeliveryAppointment() {
            return deliveryAppointment;
        }

        public void setDeliveryAppointment(FixedZoneDateTimeWindow deliveryAppointment) {
            this.deliveryAppointment = deliveryAppointment;
        }

        public Set<ShipmentUnit> getShipmentUnits() {
            return shipmentUnits;
        }

        public void setShipmentUnits(Set<ShipmentUnit> shipmentUnits) {
            this.shipmentUnits = shipmentUnits;
        }

        public BigDecimal getPayWeight() {
            return payWeight;
        }

        public void setPayWeight(BigDecimal payWeight) {
            this.payWeight = payWeight;
        }

        public String getPlanningStatus() {
            return planningStatus;
        }

        public void setPlanningStatus(String planningStatus) {
            this.planningStatus = planningStatus;
        }

        public String getShipmentStatus() {
            return shipmentStatus;
        }

        public void setShipmentStatus(String shipmentStatus) {
            this.shipmentStatus = shipmentStatus;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShipmentUnit{
        private Long id;
        private String type;
        private BigDecimal grossWeight;
        private BigDecimal netWeight;
        private BigDecimal volume;
        private BigDecimal ldm;
        private IdNamePair packageGroup;
        private Set<ShipmentUnitPackage> packages = new HashSet<>();

        public static ShipmentUnit createWith(ekol.orders.transportOrder.domain.ShipmentUnit orderShipmentUnit){
            ShipmentUnit shipmentUnit = new ShipmentUnit();
            shipmentUnit.setId(orderShipmentUnit.getId());
            shipmentUnit.setGrossWeight(orderShipmentUnit.getTotalGrossWeightInKilograms());
            shipmentUnit.setNetWeight(orderShipmentUnit.getTotalNetWeightInKilograms());
            shipmentUnit.setVolume(orderShipmentUnit.getTotalVolumeInCubicMeters());
            shipmentUnit.setLdm(orderShipmentUnit.getTotalLdm());
            if (orderShipmentUnit.getType().getPackageGroup() != null) {
                shipmentUnit.setPackageGroup(IdNamePair.createWith(
                        orderShipmentUnit.getType().getPackageGroup().getId(),
                        orderShipmentUnit.getType().getPackageGroup().getName()
                ));
            }
            orderShipmentUnit.getShipmentUnitPackages().forEach(shipmentUnitPackage -> {
                shipmentUnit.getPackages().add(ShipmentUnitPackage.createWith(shipmentUnitPackage));
            });
            return shipmentUnit;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public BigDecimal getGrossWeight() {
            return grossWeight;
        }

        public void setGrossWeight(BigDecimal grossWeight) {
            this.grossWeight = grossWeight;
        }

        public BigDecimal getNetWeight() {
            return netWeight;
        }

        public void setNetWeight(BigDecimal netWeight) {
            this.netWeight = netWeight;
        }

        public BigDecimal getVolume() {
            return volume;
        }

        public void setVolume(BigDecimal volume) {
            this.volume = volume;
        }

        public BigDecimal getLdm() {
            return ldm;
        }

        public void setLdm(BigDecimal ldm) {
            this.ldm = ldm;
        }

        public IdNamePair getPackageGroup() {
            return packageGroup;
        }

        public void setPackageGroup(IdNamePair packageGroup) {
            this.packageGroup = packageGroup;
        }

        public Set<ShipmentUnitPackage> getPackages() {
            return packages;
        }

        public void setPackages(Set<ShipmentUnitPackage> packages) {
            this.packages = packages;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShipmentUnitPackage{
        private Integer count;
        private BigDecimal length;
        private BigDecimal width;
        private BigDecimal height;
        private Integer stackSize;

        public static ShipmentUnitPackage createWith(ekol.orders.transportOrder.domain.ShipmentUnitPackage orderShipmentUnitPackage){
            ShipmentUnitPackage shipmentUnitPackage = new ShipmentUnitPackage();
            shipmentUnitPackage.setCount(orderShipmentUnitPackage.getCount());
            shipmentUnitPackage.setLength(orderShipmentUnitPackage.getLengthInCentimeters());
            shipmentUnitPackage.setWidth(orderShipmentUnitPackage.getWidthInCentimeters());
            shipmentUnitPackage.setHeight(orderShipmentUnitPackage.getHeightInCentimeters());
            shipmentUnitPackage.setStackSize(orderShipmentUnitPackage.getStackSize());
            return shipmentUnitPackage;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public BigDecimal getLength() {
            return length;
        }

        public void setLength(BigDecimal length) {
            this.length = length;
        }

        public BigDecimal getWidth() {
            return width;
        }

        public void setWidth(BigDecimal width) {
            this.width = width;
        }

        public BigDecimal getHeight() {
            return height;
        }

        public void setHeight(BigDecimal height) {
            this.height = height;
        }

        public Integer getStackSize() {
            return stackSize;
        }

        public void setStackSize(Integer stackSize) {
            this.stackSize = stackSize;
        }
    }
}
