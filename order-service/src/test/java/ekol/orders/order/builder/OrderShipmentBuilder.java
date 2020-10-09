package ekol.orders.order.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.lookup.domain.PaymentMethod;
import ekol.orders.order.domain.AmountWithCurrency;
import ekol.orders.order.domain.Appointment;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentAdr;
import ekol.orders.order.domain.OrderShipmentArrivalCustoms;
import ekol.orders.order.domain.OrderShipmentDepartureCustoms;
import ekol.orders.order.domain.OrderShipmentDocument;
import ekol.orders.order.domain.OrderShipmentEquipmentRequirement;
import ekol.orders.order.domain.OrderShipmentUnit;
import ekol.orders.order.domain.OrderShipmentVehicleRequirement;
import ekol.orders.order.domain.ShipmentHandlingParty;

public final class OrderShipmentBuilder {
    private Long id;
    private Order order;
    private Incoterm incoterm;
    private String code;
    private ShipmentHandlingParty sender;
    private ShipmentHandlingParty consignee;
    private FixedZoneDateTime readyAtDate;
    private FixedZoneDateTime deliveryDate;
    private Appointment loadingAppointment;
    private Appointment unloadingAppointment;
    private PaymentMethod paymentMethod;
    private AmountWithCurrency valueOfGoods;
    private List<OrderShipmentUnit> units = new ArrayList<>();
    private boolean hangingLoad;
    private boolean longLoad;
    private boolean oversizeLoad;
    private boolean heavyLoad;
    private boolean valuableLoad;
    private Integer totalQuantity;
    private Set<PackageType> packageTypes = new HashSet<>();
    private BigDecimal grossWeight;
    private BigDecimal netWeight;
    private BigDecimal totalVolume;
    private BigDecimal totalLdm;
    private BigDecimal payWeight;
    private boolean insured;
    private Integer temperatureMinValue;
    private Integer temperatureMaxValue;
    private List<OrderShipmentAdr> adrDetails = new ArrayList<>();
    private Set<DocumentType> healthCertificateTypes = new HashSet<>();
    private IdNameEmbeddable borderCustoms;
    private Boolean borderCrossingHealthCheck;
    private List<OrderShipmentVehicleRequirement> vehicleRequirements = new ArrayList<>();
    private List<OrderShipmentEquipmentRequirement> equipmentRequirements = new ArrayList<>();
    private Set<String> customerOrderNumbers = new HashSet<>();
    private Set<String> senderOrderNumbers = new HashSet<>();
    private Set<String> consigneeOrderNumbers = new HashSet<>();
    private OrderShipmentDepartureCustoms departureCustoms;
    private OrderShipmentArrivalCustoms arrivalCustoms;
    private List<OrderShipmentDocument> documents = new ArrayList<>();

    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private UtcDateTime deletedAt;
    private boolean deleted;

    private OrderShipmentBuilder() {
    }

    public static OrderShipmentBuilder anOrderShipment() {
        return new OrderShipmentBuilder();
    }

    public OrderShipmentBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderShipmentBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public OrderShipmentBuilder withOrder(Order order) {
        this.order = order;
        return this;
    }

    public OrderShipmentBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public OrderShipmentBuilder withIncoterm(Incoterm incoterm) {
        this.incoterm = incoterm;
        return this;
    }

    public OrderShipmentBuilder withReadyAtDate(FixedZoneDateTime readyAtDate) {
        this.readyAtDate = readyAtDate;
        return this;
    }

    public OrderShipmentBuilder withDeliveryDate(FixedZoneDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public OrderShipmentBuilder withSender(ShipmentHandlingParty sender) {
        this.sender = sender;
        return this;
    }

    public OrderShipmentBuilder withConsignee(ShipmentHandlingParty consignee) {
        this.consignee = consignee;
        return this;
    }

    public OrderShipmentBuilder withLoadingAppointment(Appointment loadingAppointment) {
        this.loadingAppointment = loadingAppointment;
        return this;
    }

    public OrderShipmentBuilder withUnloadingAppointment(Appointment unloadingAppointment) {
        this.unloadingAppointment = unloadingAppointment;
        return this;
    }

    public OrderShipmentBuilder withPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderShipmentBuilder withValueOfGoods(AmountWithCurrency valueOfGoods) {
        this.valueOfGoods = valueOfGoods;
        return this;
    }

    public OrderShipmentBuilder withUnits(List<OrderShipmentUnit> units) {
        this.units = units;
        return this;
    }

    public OrderShipmentBuilder withHangingLoad(boolean hangingLoad) {
        this.hangingLoad = hangingLoad;
        return this;
    }
    public OrderShipmentBuilder withLongLoad(boolean longLoad) {
        this.longLoad = longLoad;
        return this;
    }
    public OrderShipmentBuilder withOversizeLoad(boolean oversizeLoad) {
        this.oversizeLoad = oversizeLoad;
        return this;
    }
    public OrderShipmentBuilder withHeavyLoad(boolean heavyLoad) {
        this.heavyLoad = heavyLoad;
        return this;
    }
    public OrderShipmentBuilder withValuableLoad(boolean valuableLoad) {
        this.valuableLoad = valuableLoad;
        return this;
    }

    public OrderShipmentBuilder withTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
        return this;
    }

    public OrderShipmentBuilder withGrossWeight(BigDecimal grossWeight) {
        this.grossWeight = grossWeight;
        return this;
    }
    public OrderShipmentBuilder withNetWeight(BigDecimal netWeight) {
        this.netWeight = netWeight;
        return this;
    }
    public OrderShipmentBuilder withTotalVolume(BigDecimal totalVolume) {
        this.totalVolume = totalVolume;
        return this;
    }
    public OrderShipmentBuilder withTotalLdm(BigDecimal totalLdm) {
        this.totalLdm = totalLdm;
        return this;
    }
    public OrderShipmentBuilder withPayWeight(BigDecimal payWeight) {
        this.payWeight = payWeight;
        return this;
    }
    public OrderShipmentBuilder withInsured(boolean insured) {
        this.insured = insured;
        return this;
    }
    public OrderShipmentBuilder withPackageTypes(Set<PackageType> packageTypes) {
        this.packageTypes = packageTypes;
        return this;
    }

    public OrderShipmentBuilder withTemperatureMaxValue(Integer temperatureMaxValue) {
        this.temperatureMaxValue = temperatureMaxValue;
        return this;
    }

    public OrderShipmentBuilder withTemperatureMinValue(Integer temperatureMinValue) {
        this.temperatureMinValue = temperatureMinValue;
        return this;
    }

    public OrderShipmentBuilder withAdrDetails(List<OrderShipmentAdr> adrDetails) {
        this.adrDetails = adrDetails;
        return this;
    }

    public OrderShipmentBuilder withHealthCertificateTypes(Set<DocumentType> healthCertificateTypes) {
        this.healthCertificateTypes = healthCertificateTypes;
        return this;
    }

    public OrderShipmentBuilder withBorderCustoms(IdNameEmbeddable borderCustoms) {
        this.borderCustoms = borderCustoms;
        return this;
    }

    public OrderShipmentBuilder withBorderCrossingHealthCheck(Boolean borderCrossingHealthCheck) {
        this.borderCrossingHealthCheck = borderCrossingHealthCheck;
        return this;
    }

    public OrderShipmentBuilder withVehicleRequirements(List<OrderShipmentVehicleRequirement> vehicleRequirements) {
        this.vehicleRequirements = vehicleRequirements;
        return this;
    }

    public OrderShipmentBuilder withEquipmentRequirements(List<OrderShipmentEquipmentRequirement> equipmentRequirements) {
        this.equipmentRequirements = equipmentRequirements;
        return this;
    }

    public OrderShipmentBuilder withCustomerOrderNumbers(Set<String> customerOrderNumbers) {
        this.customerOrderNumbers = customerOrderNumbers;
        return this;
    }

    public OrderShipmentBuilder withSenderOrderNumbers(Set<String> senderOrderNumbers) {
        this.senderOrderNumbers = senderOrderNumbers;
        return this;
    }

    public OrderShipmentBuilder withConsigneeOrderNumbers(Set<String> consigneeOrderNumbers) {
        this.consigneeOrderNumbers = consigneeOrderNumbers;
        return this;
    }

    public OrderShipmentBuilder withDepartureCustoms(OrderShipmentDepartureCustoms departureCustoms) {
        this.departureCustoms = departureCustoms;
        return this;
    }

    public OrderShipmentBuilder withArrivalCustoms(OrderShipmentArrivalCustoms arrivalCustoms) {
        this.arrivalCustoms = arrivalCustoms;
        return this;
    }

    public OrderShipmentBuilder withDocuments(List<OrderShipmentDocument> documents) {
        this.documents = documents;
        return this;
    }



    public OrderShipmentBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public OrderShipmentBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public OrderShipmentBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public OrderShipmentBuilder but() {
        return anOrderShipment().withId(id).withDeleted(deleted).withOrder(order).withCode(code).withIncoterm(incoterm)
                .withReadyAtDate(readyAtDate).withDeliveryDate(deliveryDate)
                .withSender(sender).withConsignee(consignee)
                .withLoadingAppointment(loadingAppointment).withUnloadingAppointment(unloadingAppointment)
                .withPaymentMethod(paymentMethod).withValueOfGoods(valueOfGoods)
                .withHangingLoad(hangingLoad).withLongLoad(longLoad).withOversizeLoad(oversizeLoad)
                .withHeavyLoad(heavyLoad).withValuableLoad(valuableLoad)
                .withUnits(units).withTotalQuantity(totalQuantity).withPackageTypes(packageTypes).withGrossWeight(grossWeight)
                .withNetWeight(netWeight).withTotalVolume(totalVolume).withTotalLdm(totalLdm).withPayWeight(payWeight).withInsured(insured)
                .withTemperatureMinValue(temperatureMinValue).withTemperatureMaxValue(temperatureMaxValue).withAdrDetails(adrDetails)
                .withHealthCertificateTypes(healthCertificateTypes).withBorderCustoms(borderCustoms)
                .withBorderCrossingHealthCheck(borderCrossingHealthCheck)
                .withVehicleRequirements(vehicleRequirements).withEquipmentRequirements(equipmentRequirements)
                .withCustomerOrderNumbers(customerOrderNumbers).withSenderOrderNumbers(senderOrderNumbers)
                .withConsigneeOrderNumbers(consigneeOrderNumbers).withDocuments(documents)
                .withArrivalCustoms(arrivalCustoms).withDepartureCustoms(departureCustoms)
                .withLastUpdated(lastUpdated).withLastUpdatedBy(lastUpdatedBy).withDeletedAt(deletedAt);
    }

    public OrderShipment build() {
        OrderShipment orderShipment = new OrderShipment();
        orderShipment.setId(id);
        orderShipment.setDeleted(deleted);
        orderShipment.setOrder(order);
        orderShipment.setIncoterm(incoterm);
        orderShipment.setCode(code);
        orderShipment.setReadyAtDate(readyAtDate);
        orderShipment.setDeliveryDate(deliveryDate);
        orderShipment.setSender(sender);
        orderShipment.setConsignee(consignee);
        orderShipment.setLoadingAppointment(loadingAppointment);
        orderShipment.setUnloadingAppointment(unloadingAppointment);
        orderShipment.setPaymentMethod(paymentMethod);
        orderShipment.setValueOfGoods(valueOfGoods);
        orderShipment.setUnits(units);
        orderShipment.setHangingLoad(hangingLoad);
        orderShipment.setLongLoad(longLoad);
        orderShipment.setOversizeLoad(oversizeLoad);
        orderShipment.setHeavyLoad(heavyLoad);
        orderShipment.setValuableLoad(valuableLoad);
        orderShipment.setTotalQuantity(totalQuantity);
        orderShipment.setPackageTypes(packageTypes);
        orderShipment.setGrossWeight(grossWeight);
        orderShipment.setNetWeight(netWeight);
        orderShipment.setTotalVolume(totalVolume);
        orderShipment.setTotalLdm(totalLdm);
        orderShipment.setPayWeight(payWeight);
        orderShipment.setInsured(insured);
        orderShipment.setTemperatureMinValue(temperatureMinValue);
        orderShipment.setTemperatureMaxValue(temperatureMaxValue);
        orderShipment.setAdrDetails(adrDetails);
        orderShipment.setHealthCertificateTypes(healthCertificateTypes);
        orderShipment.setBorderCustoms(borderCustoms);
        orderShipment.setBorderCrossingHealthCheck(borderCrossingHealthCheck);
        orderShipment.setVehicleRequirements(vehicleRequirements);
        orderShipment.setEquipmentRequirements(equipmentRequirements);
        orderShipment.setCustomerOrderNumbers(customerOrderNumbers);
        orderShipment.setSenderOrderNumbers(senderOrderNumbers);
        orderShipment.setConsigneeOrderNumbers(consigneeOrderNumbers);
        orderShipment.setDepartureCustoms(departureCustoms);
        orderShipment.setArrivalCustoms(arrivalCustoms);
        orderShipment.setDocuments(documents);

        orderShipment.setLastUpdated(lastUpdated);
        orderShipment.setLastUpdatedBy(lastUpdatedBy);
        orderShipment.setDeletedAt(deletedAt);
        return orderShipment;
    }
}
