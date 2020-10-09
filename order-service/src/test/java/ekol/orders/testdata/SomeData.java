package ekol.orders.testdata;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.orders.builder.*;
import ekol.orders.lookup.builder.*;
import ekol.orders.lookup.domain.*;
import ekol.orders.order.domain.Status;
import ekol.orders.transportOrder.domain.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Metodlara parametre olarak sadece diğer tablolardan gelen değerleri vermek gerekiyor.
 */
public class SomeData {

    public static TransportOrderRequestBuilder someTransportOrderRequest(TransportOrder order) {
        return TransportOrderRequestBuilder.aTransportOrderRequest()
                .withCustomerId(1L)
                .withOrder(order)
                .withAcCustomerId(2L)
                .withOrderType(OrderType.SPOT)
                .withProjectNo(RandomStringUtils.randomAlphabetic(5))
                .withOfferNo(RandomStringUtils.randomAlphabetic(5))
                .withReadyAtDate(LocalDate.now(ZoneId.of("UTC")))
                .withConfirmationRequired(true)
                .withNumberOfOrders(10)
                .withCustomerNote(RandomStringUtils.randomAlphabetic(50))
                .withTaskId(RandomStringUtils.randomAlphabetic(5))
                .withCreatedById(1L)
                .withDeleted(false);
    }

    public static IncotermBuilder someIncoterm() {
        return IncotermBuilder.anIncoterm()
                .withCode(RandomStringUtils.randomAlphabetic(5))
                .withName(RandomStringUtils.randomAlphabetic(10))
                .withDescription(RandomStringUtils.randomAlphabetic(50))
                .withActive(true)
                .withDeleted(false);
    }

    public static TransportOrderBuilder someTransportOrder(TransportOrderRequest request, Incoterm incoterm) {
        return TransportOrderBuilder.aTransportOrder()
                .withRequest(request)
                .withCustomerId(1L)
                .withServiceType(ServiceType.EXPRESS)
                .withTruckLoadType(TruckLoadType.FTL)
                .withIncoterm(incoterm)
                .withInsuredByEkol(true)
                .withStatus(Status.CREATED)
                .withDeleted(false);
    }

    public static SenderOrReceiverBuilder someSenderOrReceiver() {
        return SenderOrReceiverBuilder.aSenderOrReceiver()
                .withCompanyId(1L)
                .withCompany(null)
                .withCompanyContactId(1L)
                .withCompanyContact(null)
                .withLocationOwnerCompanyId(1L)
                .withLocationOwnerCompany(null)
                .withLocationId(1L)
                .withLocation(null)
                .withLocationContactId(1L)
                .withLocationContact(null);
    }

    public static DateTimeWindowBuilder someDateTimeWindow() {
        return DateTimeWindowBuilder.aDateTimeWindow()
                .withStart(LocalDateTime.now().minusDays(2L))
                .withEnd(LocalDateTime.now().plusDays(2L));
    }

    public static FixedZoneDateTimeWindowBuilder someFixedZoneDateTimeWindow() {
        return FixedZoneDateTimeWindowBuilder.aFixedZoneDateTimeWindow()
                .withStart(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(2L).toLocalDateTime())
                .withEnd(ZonedDateTime.now(ZoneId.of("UTC")).plusDays(2L).toLocalDateTime())
                .withTimeZone("UTC");
    }

    public static AdrClassBuilder someAdrClass() {
        return AdrClassBuilder.anAdrClass()
                .withCode(RandomStringUtils.randomAlphabetic(5))
                .withName(RandomStringUtils.randomAlphabetic(10))
                .withDeleted(false);
    }

    public static ShipmentBuilder someShipment(TransportOrder transportOrder, AdrClass adrClass) {
        return ShipmentBuilder.aShipment().aShipment()
                .withTransportOrder(transportOrder)
                .withCode(RandomStringUtils.randomAlphabetic(5))
                .withAdrClass(adrClass)
                .withStatus(Status.CONFIRMED)
                .withSender(someSenderOrReceiver().build())
                .withReceiver(someSenderOrReceiver().build())
                .withReadyAtDate(new FixedZoneDateTime(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime(), "Europe/Istanbul"))
                .withRequestedDeliveryDate(new FixedZoneDateTime(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime(), "Europe/Istanbul"))
                .withPickupAppointment(someFixedZoneDateTimeWindow().build())
                .withDeliveryAppointment(someFixedZoneDateTimeWindow().build())
                .withDeleted(false);
    }

    public static ShipmentUnitBuilder someShipmentUnit(Shipment shipment, PackageType type) {
        return ShipmentUnitBuilder.aShipmentUnit()
                .withShipment(shipment)
                .withType(type)
                .withTotalGrossWeightInKilograms(new BigDecimal(100))
                .withTotalNetWeightInKilograms(new BigDecimal(99))
                .withTotalVolumeInCubicMeters(new BigDecimal(150))
                .withTotalLdm(new BigDecimal(200))
                .withDeleted(false);
    }

    public static PackageTypeBuilder somePackageType() {
        return PackageTypeBuilder.aPackageType()
                .withCode(RandomStringUtils.randomAlphabetic(5))
                .withName(RandomStringUtils.randomAlphabetic(10))
                .withDeleted(false);
    }

    public static ShipmentUnitPackageBuilder someShipmentUnitPackage(ShipmentUnit shipmentUnit) {
        return ShipmentUnitPackageBuilder.aShipmentUnitPackage()
                .withShipmentUnit(shipmentUnit)
                .withCount(10)
                .withLengthInCentimeters(new BigDecimal(100))
                .withWidthInCentimeters(new BigDecimal(150))
                .withHeightInCentimeters(new BigDecimal(200))
                .withStackSize(2)
                .withDeleted(false);
    }

    public static EquipmentTypeBuilder someEquipmentType() {
        return EquipmentTypeBuilder.anEquipmentType()
                .withCode(RandomStringUtils.randomAlphabetic(5))
                .withName(RandomStringUtils.randomAlphabetic(10))
                .withDeleted(false);
    }

    public static EquipmentRequirementBuilder someEquipmentRequirement(TransportOrder transportOrder, EquipmentType equipmentType) {
        return EquipmentRequirementBuilder.anEquipmentRequirement()
                .withTransportOrder(transportOrder)
                .withCount(3)
                .withEquipmentType(equipmentType)
                .withDeleted(false);
    }

    public static TransportTypeBuilder someTransportType() {
        return TransportTypeBuilder.aTransportType()
                .withCode(RandomStringUtils.randomAlphabetic(5))
                .withName(RandomStringUtils.randomAlphabetic(10))
                .withDeleted(false);
    }

    public static RouteRequirementBuilder someRouteRequirement(TransportOrder transportOrder, TransportType transportType) {
        return RouteRequirementBuilder.aRouteRequirement()
                .withTransportOrder(transportOrder)
                .withPermissionType(PermissionType.REQUIRED)
                .withTransportType(transportType)
                .withDeleted(false);
    }

    public static RouteRequirementRouteBuilder someRouteRequirementRoute(RouteRequirement requirement) {
        return RouteRequirementRouteBuilder.aRouteRequirementRoute()
                .withRequirement(requirement)
                .withRouteId(12L)
                .withDeleted(false);
    }

    public static VehicleRequirementBuilder someVehicleRequirement(TransportOrder transportOrder) {
        return VehicleRequirementBuilder.aVehicleRequirement()
                .withTransportOrder(transportOrder)
                .withPermissionType(PermissionType.REQUIRED)
                .withVehicleType(VehicleType.TRAILER)
                .withVehicleFeatures(new HashSet(Arrays.asList(VehicleFeature.DOUBLE_DECK, VehicleFeature.LIFTING_ROOF)))
                .withDeleted(false);
    }

    public static DocumentTypeBuilder someDocumentType() {
        return DocumentTypeBuilder.aDocumentType()
                .withCode(RandomStringUtils.randomAlphabetic(5))
                .withName(RandomStringUtils.randomAlphabetic(10))
                .withDeleted(false);
    }

    public static TransportOrderDocumentBuilder someTransportOrderDocument(TransportOrder transportOrder, DocumentType type) {
        return TransportOrderDocumentBuilder.aTransportOrderDocument()
                .withTransportOrder(transportOrder)
                .withType(type)
                .withOriginalName(RandomStringUtils.randomAlphabetic(25))
                .withDisplayName(RandomStringUtils.randomAlphabetic(25))
                .withFileName(RandomStringUtils.randomAlphabetic(15))
                .withDirectoryPath(RandomStringUtils.randomAlphabetic(100))
                .withDeleted(false);
    }

    public static TransportOrderRequestDocumentBuilder someTransportOrderRequestDocument(TransportOrderRequest request, DocumentType type) {
        return TransportOrderRequestDocumentBuilder.aTransportOrderRequestDocument()
                .withRequest(request)
                .withType(type)
                .withOriginalName(RandomStringUtils.randomAlphabetic(25))
                .withDisplayName(RandomStringUtils.randomAlphabetic(25))
                .withFileName(RandomStringUtils.randomAlphabetic(15))
                .withDirectoryPath(RandomStringUtils.randomAlphabetic(100))
                .withDeleted(false);
    }

    public static BigDecimalRangeBuilder someBigDecimalRange() {
        return BigDecimalRangeBuilder.aBigDecimalRange()
                .withMinValue(new BigDecimal("0.1234"))
                .withMaxValue(new BigDecimal("0.9876"));
    }

    public static PackageTypeRestrictionBuilder somePackageTypeRestriction(PackageType packageType) {
        return PackageTypeRestrictionBuilder.aPackageTypeRestriction()
                .withPackageType(packageType)
                .withGrossWeightRangeInKilograms(someBigDecimalRange().build())
                .withNetWeightRangeInKilograms(someBigDecimalRange().build())
                .withVolumeRangeInCubicMeters(someBigDecimalRange().build())
                .withWidthRangeInCentimeters(someBigDecimalRange().build())
                .withLengthRangeInCentimeters(someBigDecimalRange().build())
                .withHeightRangeInCentimeters(someBigDecimalRange().build())
                .withLdmRange(someBigDecimalRange().build())
                .withStackable(true)
                .withDeleted(false);
    }

    public static PaymentMethodBuilder somePaymentMethod() {
        return PaymentMethodBuilder.aPaymentMethod()
                .withCode(RandomStringUtils.randomAlphabetic(5))
                .withName(RandomStringUtils.randomAlphabetic(10))
                .withDeleted(false);
    }

    /**
     * incoterms ve paymentMethods ElementCollection olduğu için ve Incoterm ve PaymentMethod
     * başka tablolardan gelen değerler olduğu için parametrelere ekledik.
     */
    public static OrderTemplateBuilder someOrderTemplate(Set<Incoterm> incoterms, Set<PaymentMethod> paymentMethods) {

        Set<ServiceType> serviceTypes = new HashSet<>();
        serviceTypes.add(ServiceType.EXPRESS);
        serviceTypes.add(ServiceType.STANDARD);

        Set<TruckLoadType> truckLoadTypes = new HashSet<>();
        truckLoadTypes.add(TruckLoadType.FTL);
        truckLoadTypes.add(TruckLoadType.LTL);

        Set<CurrencyType> currencyTypes = new HashSet<>();
        currencyTypes.add(CurrencyType.EUR);
        currencyTypes.add(CurrencyType.TRY);

        return OrderTemplateBuilder.anOrderTemplate()
                .withCode(RandomStringUtils.randomAlphabetic(5))
                .withName(RandomStringUtils.randomAlphabetic(10))
                .withServiceTypes(serviceTypes)
                .withIncoterms(incoterms)
                .withTruckLoadTypes(truckLoadTypes)
                .withPaymentMethods(paymentMethods)
                .withCurrencyTypes(currencyTypes)
                .withDeleted(false);
    }

    public static HSCodeBuilder someHSCode() {
        return HSCodeBuilder.aHSCode()
                .withCode(RandomStringUtils.randomAlphabetic(5))
                .withName(RandomStringUtils.randomAlphabetic(10))
                .withTier(Short.valueOf("1"))
                .withDeleted(false);
    }
}
