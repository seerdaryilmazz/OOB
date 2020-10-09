package ekol.orders.order.builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import ekol.orders.builder.EquipmentTypeBuilder;
import ekol.orders.lookup.builder.AdrClassDetailsBuilder;
import ekol.orders.lookup.builder.AdrPackageTypeBuilder;
import ekol.orders.lookup.builder.DocumentTypeBuilder;
import ekol.orders.lookup.builder.IncotermBuilder;
import ekol.orders.lookup.builder.PackageGroupBuilder;
import ekol.orders.lookup.builder.PackageTypeBuilder;
import ekol.orders.lookup.builder.PaymentMethodBuilder;
import ekol.orders.lookup.domain.DocumentTypeGroup;
import ekol.orders.lookup.domain.ServiceType;
import ekol.orders.lookup.domain.TruckLoadType;
import ekol.orders.order.domain.AdrUnit;
import ekol.orders.order.domain.CompanyType;
import ekol.orders.order.domain.Status;
import ekol.orders.transportOrder.domain.OrderPlanningOperationType;
import ekol.orders.transportOrder.domain.VehicleFeature;

public class MockOrderData {

    public static IdNameEmbeddableBuilder subsidiaryTR(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(1L).withName("Ekol TR");
    }
    public static IdNameEmbeddableBuilder customer1(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(1L).withName("Customer 1");
    }
    public static IdNameEmbeddableBuilder customer2(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(2L).withName("Customer 2");
    }
    public static IdNameEmbeddableBuilder company100(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(100L).withName("Company 100");
    }
    public static IdNameEmbeddableBuilder contact100(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(100L).withName("Contact 100");
    }
    public static IdNameEmbeddableBuilder company200(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(200L).withName("Company 200");
    }
    public static IdNameEmbeddableBuilder contact200(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(200L).withName("Contact 200");
    }
    public static IdNameEmbeddableBuilder location200(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(200L).withName("Location 200");
    }
    public static IdNameEmbeddableBuilder company300(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(300L).withName("Company 300");
    }
    public static IdNameEmbeddableBuilder contact300(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(300L).withName("Contact 300");
    }
    public static IdNameEmbeddableBuilder company400(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(400L).withName("Company 400");
    }
    public static IdNameEmbeddableBuilder contact400(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(400L).withName("Contact 400");
    }
    public static IdNameEmbeddableBuilder location400(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(400L).withName("Location 400");
    }
    public static IdNameEmbeddableBuilder customsAgent1(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(500L).withName("Customs agent 1");
    }
    public static IdNameEmbeddableBuilder customsAgent2(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(600L).withName("Customs agent 2");
    }
    public static IdNameEmbeddableBuilder customsAgentLocation1(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(500L).withName("Customs agent location 1");
    }
    public static IdNameEmbeddableBuilder customsWarehouse1(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(600L).withName("Customs warehouse 1");
    }
    public static IdNameEmbeddableBuilder customsOffice1(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(1L).withName("Customs office 1");
    }
    public static CodeNameEmbeddableBuilder customsType1(){
        return CodeNameEmbeddableBuilder.aCodeNameEmbeddable().withCode("TYPE1").withName("Type 1");
    }
    public static IdNameEmbeddableBuilder region1(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(1L).withName("Region 1");
    }
    public static IdNameEmbeddableBuilder operationRegion1(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(10L).withName("Op Region 1");
    }
    public static CodeNameEmbeddableBuilder regionCategory1A(){
        return CodeNameEmbeddableBuilder.aCodeNameEmbeddable().withCode("A").withName("1A");
    }
    public static IdNameEmbeddableBuilder region2(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(2L).withName("Region 2");
    }
    public static IdNameEmbeddableBuilder operationRegion2(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(20L).withName("Op Region 2");
    }
    public static CodeNameEmbeddableBuilder regionCategory2A(){
        return CodeNameEmbeddableBuilder.aCodeNameEmbeddable().withCode("A").withName("2A");
    }
    public static FixedZoneDateTimeBuilder todayUTC(){
        return FixedZoneDateTimeBuilder.aFixedZoneDateTime().withDateTime(LocalDateTime.now()).withTimeZone("UTC");
    }
    public static FixedZoneDateTimeBuilder todayPlus2(){
        return FixedZoneDateTimeBuilder.aFixedZoneDateTime().withDateTime(LocalDateTime.now().plusDays(2)).withTimeZone("UTC");
    }
    public static FixedZoneDateTimeBuilder todayPlus7(){
        return FixedZoneDateTimeBuilder.aFixedZoneDateTime().withDateTime(LocalDateTime.now().plusDays(7)).withTimeZone("UTC");
    }
    public static IncotermBuilder incotermEXW(){
        return IncotermBuilder.anIncoterm().withId(1L).withCode("EXW").withDescription("Ex Works");
    }
    public static PaymentMethodBuilder paymentMethodFOC(){
        return PaymentMethodBuilder.aPaymentMethod().withId(1L).withCode("FOC").withName("Free of Charge");
    }
    public static PackageGroupBuilder groupPallet(){
        return PackageGroupBuilder.aPackageGroup().withId(1L).withName("Pallet");
    }
    public static PackageTypeBuilder typePallet(){
        return PackageTypeBuilder.aPackageType().withId(1L).withName("Pallet").withPackageGroup(groupPallet().build());
    }

    public static IdNameEmbeddableBuilder borderCustoms(){
        return IdNameEmbeddableBuilder.anIdNameEmbeddable().withId(1L).withName("Border");
    }
    public static DocumentTypeBuilder invoiceType(){
        return DocumentTypeBuilder.aDocumentType().withId(1L).withName("invoice");
    }
    public static DocumentTypeBuilder reservationType(){
        return DocumentTypeBuilder.aDocumentType().withId(2L).withName("reservation");
    }
    public static DocumentTypeBuilder safetyType(){
        return DocumentTypeBuilder.aDocumentType().withId(3L).withName("safety");
    }
    public static DocumentTypeBuilder entryType(){
        return DocumentTypeBuilder.aDocumentType().withId(4L).withName("entry");
    }
    public static DocumentBuilder invoiceDocument(){
        return DocumentBuilder.aDocument().withOriginalFileName("invoice.pdf").withSavedFileName("invoice-saved.pdf")
                .withType(invoiceType().build());
    }
    public static DocumentBuilder reservationDocument(){
        return DocumentBuilder.aDocument().withOriginalFileName("reservation.pdf").withSavedFileName("reservation-saved.pdf")
                .withType(reservationType().build());
    }
    public static DocumentBuilder safetyDocument(){
        return DocumentBuilder.aDocument().withOriginalFileName("safety.pdf").withSavedFileName("safety-saved.pdf")
                .withType(safetyType().build());
    }
    public static DocumentBuilder entryDocument(){
        return DocumentBuilder.aDocument().withOriginalFileName("entry.pdf").withSavedFileName("entry-saved.pdf").withType(entryType().build());
    }
    public static OrderShipmentDocumentBuilder reservationShipmentDocument(){
        return OrderShipmentDocumentBuilder.anOrderShipmentDocument().withDocument(reservationDocument().build());
    }
    public static OrderShipmentDocumentBuilder safetyAdrDocument(){
        return OrderShipmentDocumentBuilder.anOrderShipmentDocument().withDocument(safetyDocument().build());
    }
    public static OrderShipmentDocumentBuilder entryHealthDocument(){
        return OrderShipmentDocumentBuilder.anOrderShipmentDocument().withDocument(entryDocument().build());
    }
    public static OrderDocumentBuilder invoiceOrderDocument(){
        return OrderDocumentBuilder.anOrderDocument().withDocument(invoiceDocument().build());
    }
    public static OrderShipmentArrivalCustomsBuilder customsArrivalEU(){
        return OrderShipmentArrivalCustomsBuilder.anOrderShipmentArrivalCustoms()
                .withCustomsAgent(customsAgent1().build())
                .withCustomsAgentLocation(customsAgentLocation1().build());
    }
    public static OrderShipmentDepartureCustomsBuilder customsDepartureEU(){
        return OrderShipmentDepartureCustomsBuilder.anOrderShipmentDepartureCustoms()
                .withCustomsAgent(customsAgent1().build())
                .withCustomsAgentLocation(customsAgentLocation1().build());
    }
    public static OrderShipmentArrivalCustomsBuilder customsArrivalTR(){
        return OrderShipmentArrivalCustomsBuilder.anOrderShipmentArrivalCustoms()
                .withCustomsOffice(customsOffice1().build())
                .withCustomsType(customsType1().build())
                .withCustomsLocation(customsWarehouse1().build())
                .withCustomsAgent(customsAgent2().build());
    }

    public static OrderShipmentDepartureCustomsBuilder customsDepartureTR(){
        return OrderShipmentDepartureCustomsBuilder.anOrderShipmentDepartureCustoms()
                .withCustomsOffice(customsOffice1().build())
                .withCustomsAgent(customsAgent2().build());
    }

    public static OrderShipmentBuilder newShipment1(){
        return OrderShipmentBuilder.anOrderShipment().withCode("001")
                .withIncoterm(incotermEXW().build())
                .withReadyAtDate(todayPlus2().build())
                .withDeliveryDate(todayPlus7().build())
                .withSender(sender1().build())
                .withConsignee(consignee1().build())
                .withValueOfGoods(AmountWithCurrencyBuilder.anAmountWithCurrency().withAmount(BigDecimal.TEN).withCurrency("EUR").build())
                .withPaymentMethod(paymentMethodFOC().build())
                .withGrossWeight(new BigDecimal(1000))
                .withTotalVolume(new BigDecimal(10))
                .withTotalLdm(new BigDecimal(10))
                .withPackageTypes(Collections.singleton(typePallet().build()))
                .withUnits(Collections.singletonList(pallet1().build()))
                .withAdrDetails(Collections.singletonList(adrDetails1().build()))
                .withHealthCertificateTypes(Collections.singleton(healthCertificate().build()))
                .withBorderCustoms(borderCustoms().build())
                .withTemperatureMinValue(-10)
                .withTemperatureMaxValue(-5)
                .withVehicleRequirements(Collections.singletonList(boxRequirement().build()))
                .withEquipmentRequirements(Collections.singletonList(equipmentRequirement().build()))
                .withCustomerOrderNumbers(Collections.singleton("1"))
                .withSenderOrderNumbers(Collections.singleton("q"))
                .withConsigneeOrderNumbers(Collections.singleton("e"))
                .withDepartureCustoms(customsDepartureEU().build())
                .withArrivalCustoms(customsArrivalTR().build())
                .withDocuments(Arrays.asList(
                        safetyAdrDocument().build(),
                        reservationShipmentDocument().build(),
                        entryHealthDocument().build()));
    }

    public static OrderShipmentBuilder newShipmentOnlyRequired(){
        return OrderShipmentBuilder.anOrderShipment().withCode("001")
                .withIncoterm(incotermEXW().build())
                .withPaymentMethod(paymentMethodFOC().build())
                .withReadyAtDate(todayPlus2().build())
                .withDeliveryDate(todayPlus7().build())
                .withSender(sender1().build())
                .withConsignee(consignee1().build())
                .withGrossWeight(new BigDecimal(1000));
    }

    public static OrderShipmentBuilder newShipment1WithLoadingAppointment(){
        return newShipment1().withReadyAtDate(null)
                .withLoadingAppointment(AppointmentBuilder.anAppointment().withStartDateTime(todayPlus2().build()).build());
    }

    public static ShipmentHandlingPartyBuilder sender1(){
        return ShipmentHandlingPartyBuilder.aShipmentHandlingParty()
                .withCompany(company100().build()).withCompanyContact(contact100().build())
                .withHandlingCompany(company200().build())
                .withHandlingCompanyType(CompanyType.COMPANY)
                .withHandlingLocation(location200().build())
                .withHandlingContact(contact200().build())
                .withHandlingRegion(region1().build()).withHandlingOperationRegion(operationRegion1().build())
                .withHandlingRegionCategory(regionCategory1A().build())
                .withHandlingAtCrossDock(false)
                .withHandlingLocationCountryCode("AA")
                .withHandlingLocationPostalCode("34000")
                .withHandlingLocationTimezone("UTC");
    }
    public static ShipmentHandlingPartyBuilder consignee1(){
        return ShipmentHandlingPartyBuilder.aShipmentHandlingParty()
                .withCompany(company300().build()).withCompanyContact(contact300().build())
                .withHandlingCompany(company400().build())
                .withHandlingCompanyType(CompanyType.COMPANY)
                .withHandlingLocation(location400().build())
                .withHandlingContact(contact400().build())
                .withHandlingRegion(region2().build()).withHandlingOperationRegion(operationRegion2().build())
                .withHandlingRegionCategory(regionCategory2A().build())
                .withHandlingAtCrossDock(false)
                .withHandlingLocationCountryCode("TR")
                .withHandlingLocationPostalCode("16000")
                .withHandlingLocationTimezone("Europe/Istanbul");
    }

    public static OrderShipmentUnitBuilder pallet1(){
        return OrderShipmentUnitBuilder.anOrderShipmentUnit()
                .withPackageType(typePallet().build()).withQuantity(10)
                .withWidth(new BigDecimal(90)).withLength(new BigDecimal(100)).withHeight(new BigDecimal(80))
                .withVolume(BigDecimal.TEN).withLdm(BigDecimal.ONE)
                .withStackSize(3);
    }
    public static AdrClassDetailsBuilder adrClassA(){
        return AdrClassDetailsBuilder.anAdrClassDetails().withId(1L).withAdrClass("A");
    }
    public static AdrPackageTypeBuilder adrPackageTypePallet(){
        return AdrPackageTypeBuilder.anAdrPackageType().withId(1L).withName("Pallet");
    }
    public static AdrPackageTypeBuilder adrPackageTypeBox(){
        return AdrPackageTypeBuilder.anAdrPackageType().withId(2L).withName("Box");
    }
    public static DocumentTypeBuilder healthCertificate(){
        return DocumentTypeBuilder.aDocumentType().withId(1L)
                .withDocumentGroup(DocumentTypeGroup.HEALTH_CERTIFICATE).withName("Certificate");
    }
    public static OrderShipmentAdrBuilder adrDetails1(){
        return OrderShipmentAdrBuilder.anOrderShipmentAdr()
                .withAdrClassDetails(adrClassA().build())
                .withQuantity(10).withInnerQuantity(100)
                .withPackageType(AdrPackageTypeBuilder.anAdrPackageType().withId(1L).withCode("Pallet").build())
                .withInnerPackageType(AdrPackageTypeBuilder.anAdrPackageType().withId(2L).withCode("Can").build())
                .withAmount(BigDecimal.TEN).withUnit(AdrUnit.KILOGRAM);
    }

    public static OrderShipmentVehicleRequirementBuilder boxRequirement(){
        return OrderShipmentVehicleRequirementBuilder.anOrderShipmentVehicleRequirement()
                .withOperationType(OrderPlanningOperationType.COLLECTION)
                .withRequirement(VehicleFeature.BOX_BODY);
    }

    public static OrderShipmentVehicleRequirementBuilder curtainSideRequirement(){
        return OrderShipmentVehicleRequirementBuilder.anOrderShipmentVehicleRequirement()
                .withOperationType(OrderPlanningOperationType.COLLECTION)
                .withRequirement(VehicleFeature.CURTAIN_SIDER);
    }

    public static EquipmentTypeBuilder equipment(){
        return EquipmentTypeBuilder.anEquipmentType().withId(1L).withName("equipment");
    }

    public static OrderShipmentEquipmentRequirementBuilder equipmentRequirement(){
        return OrderShipmentEquipmentRequirementBuilder.anOrderShipmentEquipmentRequirement()
                .withCount(5).withEquipment(equipment().build());
    }

    public static OrderBuilder validNewOrder(){
        return OrderBuilder.anOrder()
                .withSubsidiary(subsidiaryTR().build())
                .withCode("000001")
                .withCountryCode("TR")
                .withCustomer(customer1().build())
                .withOriginalCustomer(customer2().build())
                .withServiceType(ServiceType.STANDARD)
                .withTruckLoadType(TruckLoadType.FTL)
                .withStatus(Status.CREATED)
                .withShipments(Collections.singletonList(newShipment1().build()))
                .withDocuments(Collections.singletonList(invoiceOrderDocument().build()));
    }

    public static OrderBuilder validNewOrderOnlyRequired(){
        return OrderBuilder.anOrder()
                .withSubsidiary(subsidiaryTR().build())
                .withCode("000001")
                .withCustomer(customer1().build())
                .withServiceType(ServiceType.STANDARD)
                .withTruckLoadType(TruckLoadType.FTL)
                .withStatus(Status.CREATED)
                .withShipments(Collections.singletonList(newShipmentOnlyRequired().build()));
    }
}
