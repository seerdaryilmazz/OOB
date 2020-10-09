package ekol.orders.order.builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.json.AppointmentJson;
import ekol.orders.order.domain.dto.json.IdCodeNameTrio;
import ekol.orders.order.domain.dto.json.OrderShipmentAdrJson;
import ekol.orders.order.domain.dto.json.OrderShipmentEquipmentRequirementJson;
import ekol.orders.order.domain.dto.json.OrderShipmentJson;
import ekol.orders.order.domain.dto.json.OrderShipmentUnitJson;
import ekol.orders.order.domain.dto.json.OrderShipmentVehicleRequirementJson;
import ekol.orders.order.domain.dto.json.ShipmentHandlingPartyJson;

public final class OrderShipmentJsonBuilder {
    private Long id;
    private String code;
    private IdCodeNameTrio incoterm;
    private ZonedDateTime readyAtDate;
    private ZonedDateTime deliveryDate;
    private ShipmentHandlingPartyJson sender;
    private ShipmentHandlingPartyJson consignee;
    private AppointmentJson loadingAppointment;
    private AppointmentJson unloadingAppointment;
    private BigDecimal valueOfGoods;
    private String valueOfGoodsCurrency;
    private IdCodeNameTrio paymentMethod;
    private List<OrderShipmentUnitJson> units = new ArrayList<>();
    private boolean hangingLoad;
    private boolean longLoad;
    private boolean oversizeLoad;
    private Integer totalQuantity;
    private Set<IdCodeNameTrio> packageTypes = new HashSet<>();
    private BigDecimal grossWeight;
    private BigDecimal netWeight;
    private BigDecimal totalVolume;
    private BigDecimal totalLdm;
    private boolean insured;
    private Integer temperatureMinValue;
    private Integer temperatureMaxValue;
    private List<OrderShipmentAdrJson> adrDetails = new ArrayList<>();
    private Set<IdCodeNameTrio> healthCertificateTypes = new HashSet<>();
    private IdNamePair borderCustoms;
    private Boolean borderCrossingHealthCheck;
    private List<OrderShipmentVehicleRequirementJson> vehicleRequirements = new ArrayList<>();
    private List<OrderShipmentEquipmentRequirementJson> equipmentRequirements = new ArrayList<>();
    private Set<String> customerOrderNumbers = new HashSet<>();
    private Set<String> senderOrderNumbers = new HashSet<>();
    private Set<String> consigneeOrderNumbers = new HashSet<>();

    private OrderShipmentJsonBuilder() {
    }

    public static OrderShipmentJsonBuilder anOrderShipmentJson() {
        return new OrderShipmentJsonBuilder();
    }

    public OrderShipmentJsonBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderShipmentJsonBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public OrderShipmentJsonBuilder withIncoterm(IdCodeNameTrio incoterm) {
        this.incoterm = incoterm;
        return this;
    }

    public OrderShipmentJsonBuilder withReadyAtDate(ZonedDateTime readyAtDate) {
        this.readyAtDate = readyAtDate;
        return this;
    }

    public OrderShipmentJsonBuilder withDeliveryDate(ZonedDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public OrderShipmentJsonBuilder withSender(ShipmentHandlingPartyJson sender) {
        this.sender = sender;
        return this;
    }

    public OrderShipmentJsonBuilder withConsignee(ShipmentHandlingPartyJson consignee) {
        this.consignee = consignee;
        return this;
    }

    public OrderShipmentJsonBuilder withLoadingAppointment(AppointmentJson loadingAppointment) {
        this.loadingAppointment = loadingAppointment;
        return this;
    }

    public OrderShipmentJsonBuilder withUnloadingAppointment(AppointmentJson unloadingAppointment) {
        this.unloadingAppointment = unloadingAppointment;
        return this;
    }
    public OrderShipmentJsonBuilder withPaymentMethod(IdCodeNameTrio paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }
    public OrderShipmentJsonBuilder withValueOfGoods(BigDecimal valueOfGoods) {
        this.valueOfGoods = valueOfGoods;
        return this;
    }
    public OrderShipmentJsonBuilder withValueOfGoodsCurrency(String valueOfGoodsCurrency) {
        this.valueOfGoodsCurrency = valueOfGoodsCurrency;
        return this;
    }
    public OrderShipmentJsonBuilder withUnits(List<OrderShipmentUnitJson> units) {
        this.units = units;
        return this;
    }
    public OrderShipmentJsonBuilder withHangingLoad(boolean hangingLoad) {
        this.hangingLoad = hangingLoad;
        return this;
    }
    public OrderShipmentJsonBuilder withLongLoad(boolean longLoad) {
        this.longLoad = longLoad;
        return this;
    }
    public OrderShipmentJsonBuilder withOversizeLoad(boolean oversizeLoad) {
        this.oversizeLoad = oversizeLoad;
        return this;
    }
    public OrderShipmentJsonBuilder withTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
        return this;
    }

    public OrderShipmentJsonBuilder withGrossWeight(BigDecimal grossWeight) {
        this.grossWeight = grossWeight;
        return this;
    }
    public OrderShipmentJsonBuilder withNetWeight(BigDecimal netWeight) {
        this.netWeight = netWeight;
        return this;
    }
    public OrderShipmentJsonBuilder withTotalVolume(BigDecimal totalVolume) {
        this.totalVolume = totalVolume;
        return this;
    }
    public OrderShipmentJsonBuilder withTotalLdm(BigDecimal totalLdm) {
        this.totalLdm = totalLdm;
        return this;
    }
    public OrderShipmentJsonBuilder withInsured(boolean insured) {
        this.insured = insured;
        return this;
    }
    public OrderShipmentJsonBuilder withPackageTypes(Set<IdCodeNameTrio> packageTypes) {
        this.packageTypes = packageTypes;
        return this;
    }
    public OrderShipmentJsonBuilder withTemperatureMaxValue(Integer temperatureMaxValue) {
        this.temperatureMaxValue = temperatureMaxValue;
        return this;
    }

    public OrderShipmentJsonBuilder withTemperatureMinValue(Integer temperatureMinValue) {
        this.temperatureMinValue = temperatureMinValue;
        return this;
    }

    public OrderShipmentJsonBuilder withAdrDetails(List<OrderShipmentAdrJson> adrDetails) {
        this.adrDetails = adrDetails;
        return this;
    }

    public OrderShipmentJsonBuilder withHealthCertificateTypes(Set<IdCodeNameTrio> healthCertificateTypes) {
        this.healthCertificateTypes = healthCertificateTypes;
        return this;
    }
    public OrderShipmentJsonBuilder withBorderCustoms(IdNamePair borderCustoms) {
        this.borderCustoms = borderCustoms;
        return this;
    }
    public OrderShipmentJsonBuilder withBorderCrossingHealthCheck(Boolean borderCrossingHealthCheck) {
        this.borderCrossingHealthCheck = borderCrossingHealthCheck;
        return this;
    }
    public OrderShipmentJsonBuilder withVehicleRequirements(List<OrderShipmentVehicleRequirementJson> vehicleRequirements) {
        this.vehicleRequirements = vehicleRequirements;
        return this;
    }
    public OrderShipmentJsonBuilder withEquipmentRequirements(List<OrderShipmentEquipmentRequirementJson> equipmentRequirements) {
        this.equipmentRequirements = equipmentRequirements;
        return this;
    }
    public OrderShipmentJsonBuilder withCustomerOrderNumbers(Set<String> customerOrderNumbers) {
        this.customerOrderNumbers = customerOrderNumbers;
        return this;
    }

    public OrderShipmentJsonBuilder withSenderOrderNumbers(Set<String> senderOrderNumbers) {
        this.senderOrderNumbers = senderOrderNumbers;
        return this;
    }

    public OrderShipmentJsonBuilder withConsigneeOrderNumbers(Set<String> consigneeOrderNumbers) {
        this.consigneeOrderNumbers = consigneeOrderNumbers;
        return this;
    }


    public OrderShipmentJsonBuilder but() {
        return anOrderShipmentJson().withId(id).withCode(code).withIncoterm(incoterm).withReadyAtDate(readyAtDate).withDeliveryDate(deliveryDate)
                .withSender(sender).withConsignee(consignee).withLoadingAppointment(loadingAppointment).withUnloadingAppointment(unloadingAppointment)
                .withPaymentMethod(paymentMethod).withValueOfGoods(valueOfGoods).withValueOfGoodsCurrency(valueOfGoodsCurrency)
                .withHangingLoad(hangingLoad).withLongLoad(longLoad).withOversizeLoad(oversizeLoad)
                .withUnits(units).withTotalQuantity(totalQuantity).withPackageTypes(packageTypes)
                .withGrossWeight(grossWeight).withNetWeight(netWeight).withTotalVolume(totalVolume).withTotalLdm(totalLdm).withInsured(insured)
                .withTemperatureMinValue(temperatureMinValue).withTemperatureMaxValue(temperatureMaxValue)
                .withAdrDetails(adrDetails).withHealthCertificateTypes(healthCertificateTypes).withBorderCustoms(borderCustoms)
                .withVehicleRequirements(vehicleRequirements).withEquipmentRequirements(equipmentRequirements)
                .withBorderCrossingHealthCheck(borderCrossingHealthCheck).withCustomerOrderNumbers(customerOrderNumbers)
                .withSenderOrderNumbers(senderOrderNumbers).withConsigneeOrderNumbers(consigneeOrderNumbers);
    }

    public OrderShipmentJson build() {
        OrderShipmentJson orderShipmentJson = new OrderShipmentJson();
        orderShipmentJson.setId(id);
        orderShipmentJson.setCode(code);
        orderShipmentJson.setIncoterm(incoterm);
        orderShipmentJson.setReadyAtDate(readyAtDate);
        orderShipmentJson.setDeliveryDate(deliveryDate);
        orderShipmentJson.setSender(sender);
        orderShipmentJson.setConsignee(consignee);
        orderShipmentJson.setLoadingAppointment(loadingAppointment);
        orderShipmentJson.setUnloadingAppointment(unloadingAppointment);
        orderShipmentJson.setPaymentMethod(paymentMethod);
        orderShipmentJson.setValueOfGoods(valueOfGoods);
        orderShipmentJson.setValueOfGoodsCurrency(valueOfGoodsCurrency);
        orderShipmentJson.setUnits(units);
        orderShipmentJson.setHangingLoad(hangingLoad);
        orderShipmentJson.setLongLoad(longLoad);
        orderShipmentJson.setOversizeLoad(oversizeLoad);
        orderShipmentJson.setTotalQuantity(totalQuantity);
        orderShipmentJson.setPackageTypes(packageTypes);
        orderShipmentJson.setGrossWeight(grossWeight);
        orderShipmentJson.setNetWeight(netWeight);
        orderShipmentJson.setTotalVolume(totalVolume);
        orderShipmentJson.setTotalLdm(totalLdm);
        orderShipmentJson.setInsured(insured);
        orderShipmentJson.setTemperatureMinValue(temperatureMinValue);
        orderShipmentJson.setTemperatureMaxValue(temperatureMaxValue);
        orderShipmentJson.setAdrDetails(adrDetails);
        orderShipmentJson.setHealthCertificateTypes(healthCertificateTypes);
        orderShipmentJson.setBorderCustoms(borderCustoms);
        orderShipmentJson.setBorderCrossingHealthCheck(borderCrossingHealthCheck);
        orderShipmentJson.setVehicleRequirements(vehicleRequirements);
        orderShipmentJson.setEquipmentRequirements(equipmentRequirements);
        orderShipmentJson.setCustomerOrderNumbers(customerOrderNumbers);
        orderShipmentJson.setSenderOrderNumbers(senderOrderNumbers);
        orderShipmentJson.setConsigneeOrderNumbers(consigneeOrderNumbers);

        return orderShipmentJson;
    }
}
