package ekol.orders.order.domain.dto.json;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.model.IdNamePair;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.lookup.domain.PaymentMethod;
import ekol.orders.order.domain.AmountWithCurrency;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentDefinitionOfGoods;
import ekol.orders.order.domain.OrderShipmentIdMapping;
import ekol.orders.order.domain.serializer.ZonedDateTimeDeserializer;
import ekol.orders.order.domain.serializer.ZonedDateTimeSerializer;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderShipmentJson {

    private Long id;
    private String code;
    private IdCodeNameTrio incoterm;
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime readyAtDate;
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime deliveryDate;
    private ShipmentHandlingPartyJson sender;
    private ShipmentHandlingPartyJson consignee;
    private ShipmentPartyJson manufacturer;
    private AppointmentJson loadingAppointment;
    private AppointmentJson unloadingAppointment;
    private BigDecimal valueOfGoods;
    private String valueOfGoodsCurrency;
    private IdCodeNameTrio paymentMethod;
    private List<OrderShipmentUnitJson> units = new ArrayList<>();
    private List<OrderShipmentDefinitionOfGoodsJson> definitionOfGoods = new ArrayList<>();
    private boolean hangingLoad;
    private boolean longLoad;
    private boolean oversizeLoad;
    private boolean heavyLoad;
    private boolean valuableLoad;
    private Integer totalQuantity;
    private Set<IdCodeNameTrio> packageTypes = new HashSet<>();
    private BigDecimal grossWeight;
    private BigDecimal netWeight;
    private BigDecimal totalVolume;
    private BigDecimal totalLdm;
    private BigDecimal payWeight;
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
    private Set<String> loadingOrderNumbers = new HashSet<>();
    private Set<String> unloadingOrderNumbers = new HashSet<>();
    private OrderShipmentCustomsJson departureCustoms;
    private OrderShipmentCustomsJson arrivalCustoms;
    private List<DocumentJson> documents = new ArrayList<>();
    private Map<String,String> mappedIds = new HashMap<>();
    private String createdBy;
    private String lastUpdatedBy;


    public OrderShipment toEntity(){
        OrderShipment shipment = new OrderShipment();
        shipment.setId(getId());
        shipment.setCode(getCode());
        shipment.setIncoterm(Incoterm.with(getIncoterm()));
        if(getReadyAtDate() != null){
            shipment.setReadyAtDate(new FixedZoneDateTime(getReadyAtDate().toLocalDateTime(), getReadyAtDate().getZone().getId()));
        }
        if(getDeliveryDate() != null){
            shipment.setDeliveryDate(new FixedZoneDateTime(getDeliveryDate().toLocalDateTime(), getDeliveryDate().getZone().getId()));
        }

        shipment.setSender(getSender().toEntity());
        shipment.setConsignee(getConsignee().toEntity());
        shipment.setManufacturer(Optional.ofNullable(getManufacturer()).map(ShipmentPartyJson::toEntity).orElse(null));
        if(getLoadingAppointment() != null){
            shipment.setLoadingAppointment(getLoadingAppointment().toEntity());
        }
        if(getUnloadingAppointment() != null){
            shipment.setUnloadingAppointment(getUnloadingAppointment().toEntity());
        }

        shipment.setPaymentMethod(PaymentMethod.with(getPaymentMethod()));
        if(getValueOfGoods() != null){
            shipment.setValueOfGoods(AmountWithCurrency.with(getValueOfGoods(), getValueOfGoodsCurrency()));
        }

        shipment.setUnits(getUnits().stream().map(OrderShipmentUnitJson::toEntity).collect(toList()));
        shipment.setDefinitionOfGoods(getDefinitionOfGoods().stream().map(OrderShipmentDefinitionOfGoods::with).collect(toList()));
        shipment.setHangingLoad(isHangingLoad());
        shipment.setLongLoad(isLongLoad());
        shipment.setOversizeLoad(isOversizeLoad());
        shipment.setHeavyLoad(isHeavyLoad());
        shipment.setValuableLoad(isValuableLoad());
        shipment.setTotalQuantity(getTotalQuantity());
        shipment.setPackageTypes(getPackageTypes().stream().map(PackageType::with).collect(toSet()));
        shipment.setGrossWeight(getGrossWeight());
        shipment.setNetWeight(getNetWeight());
        shipment.setTotalVolume(getTotalVolume());
        shipment.setTotalLdm(getTotalLdm());
        shipment.setPayWeight(getPayWeight());
        shipment.setInsured(isInsured());
        shipment.setTemperatureMinValue(getTemperatureMinValue());
        shipment.setTemperatureMaxValue(getTemperatureMaxValue());
        shipment.setAdrDetails(getAdrDetails().stream().map(OrderShipmentAdrJson::toEntity).collect(toList()));
        shipment.setHealthCertificateTypes(getHealthCertificateTypes().stream().map(DocumentType::with).collect(toSet()));
        if(getBorderCustoms() != null){
            shipment.setBorderCustoms(IdNameEmbeddable.with(getBorderCustoms()));
        }
        shipment.setBorderCrossingHealthCheck(getBorderCrossingHealthCheck());
        shipment.setVehicleRequirements(
                getVehicleRequirements().stream().map(OrderShipmentVehicleRequirementJson::toEntity).collect(toList()));
        shipment.setEquipmentRequirements(
                getEquipmentRequirements().stream().map(OrderShipmentEquipmentRequirementJson::toEntity).collect(toList()));

        shipment.setCustomerOrderNumbers(getCustomerOrderNumbers());
        shipment.setSenderOrderNumbers(getSenderOrderNumbers());
        shipment.setConsigneeOrderNumbers(getConsigneeOrderNumbers());
        shipment.setLoadingOrderNumbers(getLoadingOrderNumbers());
        shipment.setUnloadingOrderNumbers(getUnloadingOrderNumbers());

        if(getDepartureCustoms() != null){
            shipment.setDepartureCustoms(getDepartureCustoms().toDepartureEntity());
        }
        if(getArrivalCustoms() != null){
            shipment.setArrivalCustoms(getArrivalCustoms().toArrivalEntity());
        }

        shipment.setDocuments(getDocuments().stream().map(DocumentJson::toShipmentDocument).collect(toList()));
        if(!CollectionUtils.isEmpty(getMappedIds())) {
        	shipment.setMappedIds(getMappedIds().entrySet().stream().map(i->OrderShipmentIdMapping.withApplication(i.getKey(), i.getValue())).collect(toList()));
        }
        return shipment;
    }

    public static OrderShipmentJson fromEntity(OrderShipment orderShipment){
        OrderShipmentJson json = new OrderShipmentJson();
        json.setId(orderShipment.getId());
        json.setCode(orderShipment.getCode());
        Optional.ofNullable(orderShipment.getIncoterm()).map(IdCodeNameTrio::with).ifPresent(json::setIncoterm);
        if(orderShipment.getReadyAtDate() != null){
            json.setReadyAtDate(orderShipment.getReadyAtDate().toZonedDateTime());
        }
        if(orderShipment.getDeliveryDate() != null){
            json.setDeliveryDate(orderShipment.getDeliveryDate().toZonedDateTime());
        }
        json.setSender(ShipmentHandlingPartyJson.fromEntity(orderShipment.getSender()));
        json.setConsignee(ShipmentHandlingPartyJson.fromEntity(orderShipment.getConsignee()));
        json.setManufacturer(Optional.ofNullable(orderShipment.getManufacturer()).map(ShipmentPartyJson::fromEntity).orElse(null));
        if(orderShipment.getLoadingAppointment() != null){
            json.setLoadingAppointment(AppointmentJson.fromEntity(orderShipment.getLoadingAppointment()));
        }
        if(orderShipment.getUnloadingAppointment() != null){
            json.setUnloadingAppointment(AppointmentJson.fromEntity(orderShipment.getUnloadingAppointment()));
        }
        Optional.ofNullable(orderShipment.getPaymentMethod()).map(IdCodeNameTrio::with).ifPresent(json::setPaymentMethod);
        if(orderShipment.getValueOfGoods() != null){
            json.setValueOfGoods(orderShipment.getValueOfGoods().getAmount());
            json.setValueOfGoodsCurrency(orderShipment.getValueOfGoods().getCurrency());
        }
        json.setUnits(orderShipment.getUnits().stream().map(OrderShipmentUnitJson::fromEntity).collect(toList()));
        json.setDefinitionOfGoods(orderShipment.getDefinitionOfGoods().stream().map(OrderShipmentDefinitionOfGoodsJson::with).collect(toList()));
        json.setHangingLoad(orderShipment.isHangingLoad());
        json.setLongLoad(orderShipment.isLongLoad());
        json.setOversizeLoad(orderShipment.isOversizeLoad());
        json.setHeavyLoad(orderShipment.isHeavyLoad());
        json.setValuableLoad(orderShipment.isValuableLoad());
        json.setTotalQuantity(orderShipment.getTotalQuantity());
        json.setPackageTypes(orderShipment.getPackageTypes().stream().map(IdCodeNameTrio::with).collect(toSet()));
        json.setGrossWeight(orderShipment.getGrossWeight());
        json.setNetWeight(orderShipment.getNetWeight());
        json.setTotalVolume(orderShipment.getTotalVolume());
        json.setTotalLdm(orderShipment.getTotalLdm());
        json.setPayWeight(orderShipment.getPayWeight());
        json.setInsured(orderShipment.isInsured());
        json.setTemperatureMinValue(orderShipment.getTemperatureMinValue());
        json.setTemperatureMaxValue(orderShipment.getTemperatureMaxValue());
        json.setAdrDetails(orderShipment.getAdrDetails().stream().map(OrderShipmentAdrJson::fromEntity).collect(toList()));
        json.setHealthCertificateTypes(orderShipment.getHealthCertificateTypes().stream().map(IdCodeNameTrio::with).collect(toSet()));
        if(orderShipment.getBorderCustoms() != null){
            json.setBorderCustoms(orderShipment.getBorderCustoms().toIdNamePair());
        }

        json.setBorderCrossingHealthCheck(orderShipment.getBorderCrossingHealthCheck());
        json.setVehicleRequirements(orderShipment.getVehicleRequirements().stream()
                .map(OrderShipmentVehicleRequirementJson::fromEntity).collect(toList()));
        json.setEquipmentRequirements(orderShipment.getEquipmentRequirements().stream()
                .map(OrderShipmentEquipmentRequirementJson::fromEntity).collect(toList()));

        if(orderShipment.getDepartureCustoms() != null){
            json.setDepartureCustoms(OrderShipmentCustomsJson.fromEntity(orderShipment.getDepartureCustoms()));
        }
        if(orderShipment.getArrivalCustoms() != null){
            json.setArrivalCustoms(OrderShipmentCustomsJson.fromEntity(orderShipment.getArrivalCustoms()));
        }

        Optional.ofNullable(orderShipment.getCustomerOrderNumbers()).ifPresent(a->json.setCustomerOrderNumbers(a.stream().filter(StringUtils::isNotEmpty).collect(toSet())));
        Optional.ofNullable(orderShipment.getSenderOrderNumbers()).ifPresent(a->json.setSenderOrderNumbers(a.stream().filter(StringUtils::isNotEmpty).collect(toSet())));
        Optional.ofNullable(orderShipment.getConsigneeOrderNumbers()).ifPresent(a->json.setConsigneeOrderNumbers(a.stream().filter(StringUtils::isNotEmpty).collect(toSet())));
        Optional.ofNullable(orderShipment.getLoadingOrderNumbers()).ifPresent(a->json.setLoadingOrderNumbers(a.stream().filter(StringUtils::isNotEmpty).collect(toSet())));
        Optional.ofNullable(orderShipment.getUnloadingOrderNumbers()).ifPresent(a->json.setUnloadingOrderNumbers(a.stream().filter(StringUtils::isNotEmpty).collect(toSet())));
        json.setDocuments(orderShipment.getDocuments().stream().map(DocumentJson::fromShipmentDocument).collect(toList()));
        json.setMappedIds(orderShipment.getMappedIds().stream().collect(toMap(OrderShipmentIdMapping::getApplication, OrderShipmentIdMapping::getApplicationOrderShipmentId)));
        json.setCreatedBy(orderShipment.getCreatedBy());
        json.setLastUpdatedBy(orderShipment.getLastUpdatedBy());
        return json;
    }
}
