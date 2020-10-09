package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.lookup.builder.DocumentTypeBuilder;
import ekol.orders.lookup.domain.DocumentTypeGroup;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import ekol.orders.lookup.repository.IncotermRepository;
import ekol.orders.lookup.repository.PaymentMethodRepository;
import ekol.orders.order.builder.AmountWithCurrencyBuilder;
import ekol.orders.order.builder.CountryResponseBuilder;
import ekol.orders.order.builder.OrderShipmentArrivalCustomsBuilder;
import ekol.orders.order.builder.OrderShipmentDepartureCustomsBuilder;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentArrivalCustoms;
import ekol.orders.order.domain.OrderShipmentDepartureCustoms;
import ekol.orders.order.domain.OrderShipmentDocument;
import ekol.orders.order.service.LocationServiceClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static ekol.orders.order.builder.MockOrderData.*;
import static java.util.stream.Collectors.toList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class OrderShipmentValidatorUnitTest {

    private OrderShipmentValidator validator;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private IncotermRepository incotermRepository;
    @MockBean
    private PaymentMethodRepository paymentMethodRepository;
    @MockBean
    private DocumentTypeRepository documentTypeRepository;
    @MockBean
    private LocationServiceClient locationServiceClient;
    @MockBean
    private OrderShipmentDateValidator orderShipmentDateValidator;
    @MockBean
    private OrderHandlingPartyValidator orderHandlingPartyValidator;
    @MockBean
    private OrderShipmentUnitValidator orderShipmentUnitValidator;
    @MockBean
    private OrderShipmentCustomsValidator orderShipmentCustomsValidator;
    @MockBean
    private OrderShipmentAdrValidator orderShipmentAdrValidator;
    @MockBean
    private OrderShipmentVehicleValidator orderShipmentVehicleValidator;
    @MockBean
    private OrderShipmentEquipmentValidator orderShipmentEquipmentValidator;
    @MockBean
    private DocumentValidator documentValidator;

    @Before
    public void init(){
        this.validator = new OrderShipmentValidator(orderHandlingPartyValidator,
                orderShipmentDateValidator,
                orderShipmentUnitValidator,
                orderShipmentCustomsValidator,
                orderShipmentAdrValidator,
                orderShipmentVehicleValidator,
                orderShipmentEquipmentValidator,
                documentValidator,
                incotermRepository,
                paymentMethodRepository,
                documentTypeRepository,
                locationServiceClient);

        given(incotermRepository.findById(1L)).willReturn(Optional.of(incotermEXW().build()));
        given(incotermRepository.findById(2L)).willReturn(Optional.empty());

        given(paymentMethodRepository.findById(1L)).willReturn(Optional.of(paymentMethodFOC().build()));
        given(paymentMethodRepository.findById(2L)).willReturn(Optional.empty());

        given(documentTypeRepository.findById(1L)).willReturn(Optional.of(healthCertificate().build()));
        given(documentTypeRepository.findById(2L)).willReturn(Optional.empty());
        given(documentTypeRepository.findById(3L)).willReturn(
                Optional.of(healthCertificate().withDocumentGroup(DocumentTypeGroup.DANGEROUS_GOODS).build())
        );

        given(locationServiceClient.isCustomsOfficeExists(1L)).willReturn(true);
        given(locationServiceClient.isCustomsOfficeExists(2L)).willReturn(false);

        given(locationServiceClient.getCountryDetails("AA")).willReturn(
                CountryResponseBuilder.aCountryResponse().withEuMember(true).withIso("AA").withId(1L).withName("A").build()
        );
        given(locationServiceClient.getCountryDetails("BB")).willReturn(
                CountryResponseBuilder.aCountryResponse().withEuMember(true).withIso("BB").withId(2L).withName("B").build()
        );

        given(locationServiceClient.getCountryDetails("TR")).willReturn(
                CountryResponseBuilder.aCountryResponse().withEuMember(false).withIso("TR").withId(3L).withName("Turkey").build()
        );
    }

    private void callValidateAndExpectException(OrderShipment orderShipment, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validate(orderShipment);

    }

    private void callValidateTotalMeasurementsAndExpectException(OrderShipment orderShipment, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validateTotalMeasurements(orderShipment);
    }

    private void callValidateLoadSpecsAndExpectException(OrderShipment orderShipment, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validateLoadSpecs(orderShipment);
    }

    private void callValidateDocumentsAndExpectException(OrderShipment orderShipment, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validateDocuments(orderShipment);
    }

    @Test
    public void givenValidShipment_whenValidateNew_thenThrowNoException(){
        OrderShipment shipment = newShipment1().build();

        validator.validate(shipment);

        then(orderShipmentDateValidator).should(times(1)).validate(shipment);
        then(orderHandlingPartyValidator).should(times(1)).validateSender(shipment.getSender());
        then(orderHandlingPartyValidator).should(times(1)).validateConsignee(shipment.getConsignee());
        then(orderShipmentAdrValidator).should(times(1)).validate(shipment.getAdrDetails().get(0));
        then(orderShipmentEquipmentValidator).should(times(1)).validate(shipment);
        then(orderShipmentVehicleValidator).should(times(1)).validate(shipment);

        shipment.getUnits().forEach(shipmentUnit ->
                then(orderShipmentUnitValidator).should(times(1)).validate(shipmentUnit));

    }

    @Test
    public void givenValidShipmentWithOnlyRequiredData_whenValidateNew_thenThrowNoException(){
        OrderShipment shipment = newShipmentOnlyRequired().build();

        validator.validate(shipment);

        then(orderShipmentDateValidator).should(times(1)).validate(shipment);
        then(orderHandlingPartyValidator).should(times(1)).validateSender(shipment.getSender());
        then(orderHandlingPartyValidator).should(times(1)).validateConsignee(shipment.getConsignee());

        shipment.getUnits().forEach(shipmentUnit ->
                then(orderShipmentUnitValidator).should(times(1)).validate(shipmentUnit));

    }

    @Test
    public void givenValidShipmentWithDocuments_whenValidateDocuments_thenThrowNoException(){
        OrderShipment shipment = newShipment1().build();

        validator.validateDocuments(shipment);

        then(documentValidator).should(times(1)).validate(
                shipment.getDocuments().stream().map(OrderShipmentDocument::getDocument).collect(toList()));
    }

    @Test
    public void givenShipmentWithShipmentId_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withId(1L).build(),
                "shipment should not have an ID");
    }

    @Test
    public void givenShipmentWithEmptyValueOfGoodsCurrency_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withValueOfGoods(
                                AmountWithCurrencyBuilder.anAmountWithCurrency().withAmount(BigDecimal.TEN).withCurrency(null).build()
                        ).build(),
                "value of goods should have amount and currency");
    }

    @Test
    public void givenShipmentWithEmptyValueOfGoodsAmount_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withValueOfGoods(
                                AmountWithCurrencyBuilder.anAmountWithCurrency().withAmount(null).withCurrency("EUR").build()
                        ).build(),
                "value of goods should have amount and currency");
    }

    @Test
    public void givenShipmentWithHangingAndLongLoads_whenValidateNew_thenThrowException() {
        callValidateLoadSpecsAndExpectException(newShipment1().withHangingLoad(true).withLongLoad(true).build(),
                "shipment should not have hanging loads and long loads together");
    }

    @Test
    public void givenShipmentWithSameHandlingLocations_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1()
                        .withConsignee(consignee1().withHandlingLocation(location200().build()).build())
                        .withSender(sender1().withHandlingLocation(location200().build()).build())
                        .build(),
                "sender and consignee handling should not have the same location");
    }

    /*
     incoterm validations
    */

    @Test
    public void givenShipmentWithEmptyIncoterm_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withIncoterm(null).build(),
                "should have an incoterm");
    }
    @Test
    public void givenShipmentWithEmptyIncotermId_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withIncoterm(incotermEXW().withId(null).build()).build(),
                "should have an incoterm");
    }
    @Test
    public void givenShipmentWithInvalidIncotermId_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withIncoterm(incotermEXW().withId(2L).build()).build(),
                "Incoterm with id 2 does not exist");
    }

    /*
     payment method validations
    */

    @Test
    public void givenShipmentWithEmptyPaymentMethod_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withPaymentMethod(null).build(),
                "should have a payment method");
    }
    @Test
    public void givenShipmentWithEmptyPaymentMethodId_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withPaymentMethod(paymentMethodFOC().withId(null).build()).build(),
                "should have a payment method");
    }
    @Test
    public void givenShipmentWithInvalidPaymentMethodId_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withPaymentMethod(paymentMethodFOC().withId(2L).build()).build(),
                "Payment method with id 2 does not exist");
    }

    /*
     shipment total measurement validations
    */
    @Test
    public void givenShipmentUnitWithEmptyTotalWeightLdmVolume_whenValidateNew_thenThrowException() {
        callValidateTotalMeasurementsAndExpectException(
                newShipment1().withGrossWeight(null).withTotalLdm(null).withTotalVolume(null).build(),
                "shipment should have a one of gross weight, total ldm or total volume");
    }

    @Test
    public void givenShipmentUnitWithGrossWeightSmallerThanNetWeight_whenValidateNew_thenThrowException() {
        callValidateTotalMeasurementsAndExpectException(
                newShipment1().withGrossWeight(BigDecimal.ONE).withNetWeight(BigDecimal.TEN).build(),
                "shipment should have a gross weight greater than net weight");
    }

    @Test
    public void givenShipmentUnitWithGroupsLessThanUnitGroups_whenValidateNew_thenThrowException() {
        callValidateTotalMeasurementsAndExpectException(
                newShipment1().withPackageTypes(Collections.emptySet()).build(),
                "shipment should have package groups according to shipment unit package types");
    }

    @Test
    public void givenShipmentUnitWithTotalQuantitySmallerThanUnitQuantities_whenValidateNew_thenThrowException() {
        callValidateTotalMeasurementsAndExpectException(
                newShipment1().withTotalQuantity(5).build(),
                "shipment total quantity should be greater than 10");
    }

    @Test
    public void givenShipmentUnitWithTotalVolumeSmallerThanUnitVolumes_whenValidateNew_thenThrowException() {
        callValidateTotalMeasurementsAndExpectException(
                newShipment1().withTotalVolume(new BigDecimal(.5)).build(),
                "shipment total volume should be greater than 10.00");
    }
    @Test
    public void givenShipmentUnitWithTotalLdmSmallerThanUnitLdm_whenValidateNew_thenThrowException() {
        callValidateTotalMeasurementsAndExpectException(
                newShipment1().withTotalLdm(new BigDecimal(.5)).build(),
                "shipment total ldm should be greater than 1.00");
    }

    /*
    validate temperature & ADR & health certificates
     */

    @Test
    public void givenShipmentWithMinTemperatureHigher_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withTemperatureMinValue(0).withTemperatureMaxValue(-2).build(),
                "should have temperature max. value higher than temperature min. value");
    }

    @Test
    public void givenShipmentWithEmptyHealthCertificate_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withHealthCertificateTypes(
                Collections.singleton(null)).build(),
                "shipment health certificate should have document type");
    }
    @Test
    public void givenShipmentWithEmptyHealthCertificateId_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withHealthCertificateTypes(
                Collections.singleton(DocumentTypeBuilder.aDocumentType().withId(null).build())).build(),
                "shipment health certificate should have document type");
    }
    @Test
    public void givenShipmentWithInvalidHealthCertificateId_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withHealthCertificateTypes(
                Collections.singleton(
                        DocumentTypeBuilder.aDocumentType().withId(2L).build()
                )).build(),
                "Document type with id 2 does not exist");
    }

    @Test
    public void givenShipmentWithInvalidHealthCertificateGroup_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(newShipment1().withHealthCertificateTypes(
                Collections.singleton(
                        DocumentTypeBuilder.aDocumentType().withId(3L).build()
                )).build(),
                "shipment health certificate should have document type with group ''Health Certificate''");
    }

    @Test
    public void givenShipmentWithInvalidBorderCrossing_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(
                newShipment1().withBorderCustoms(borderCustoms().withId(2L).build()).build(),
                "Border crossing with id 2 does not exist");
    }


    /*
    validate customs
     */
    @Test
    public void givenShipmentInSameCountry_whenValidateCustoms_thenDontValidate() {
        OrderShipment shipment = newShipment1()
                .withDepartureCustoms(null).withArrivalCustoms(null)
                .withSender(sender1().withHandlingLocationCountryCode("AA").build())
                .withConsignee(consignee1().withHandlingLocationCountryCode("AA").build()).build();

        validator.validateCustoms(shipment);

        then(orderShipmentCustomsValidator).should(never()).validateDepartureFromTR(any(OrderShipmentDepartureCustoms.class));
        then(orderShipmentCustomsValidator).should(never()).validateDeparture(any(OrderShipmentDepartureCustoms.class));
        then(orderShipmentCustomsValidator).should(never()).validateArrivalToTR(any(OrderShipmentArrivalCustoms.class));
        then(orderShipmentCustomsValidator).should(never()).validateArrival(any(OrderShipmentArrivalCustoms.class));

    }

    @Test
    public void givenShipmentInEU_whenValidateCustoms_thenDontValidate() {
        OrderShipment shipment = newShipment1()
                .withDepartureCustoms(null).withArrivalCustoms(null)
                .withSender(sender1().withHandlingLocationCountryCode("AA").build())
                .withConsignee(consignee1().withHandlingLocationCountryCode("BB").build()).build();

        validator.validateCustoms(shipment);

        then(orderShipmentCustomsValidator).should(never()).validateDepartureFromTR(any(OrderShipmentDepartureCustoms.class));
        then(orderShipmentCustomsValidator).should(never()).validateDeparture(any(OrderShipmentDepartureCustoms.class));
        then(orderShipmentCustomsValidator).should(never()).validateArrivalToTR(any(OrderShipmentArrivalCustoms.class));
        then(orderShipmentCustomsValidator).should(never()).validateArrival(any(OrderShipmentArrivalCustoms.class));

    }

    @Test
    public void givenShipmentEUTR_whenValidateCustoms_thenValidateDepartureEUAndArrivalTR() {
        OrderShipmentDepartureCustoms departureCustoms = OrderShipmentDepartureCustomsBuilder.anOrderShipmentDepartureCustoms().build();
        OrderShipmentArrivalCustoms arrivalCustoms = OrderShipmentArrivalCustomsBuilder.anOrderShipmentArrivalCustoms().build();
        OrderShipment shipment = newShipment1()
                .withDepartureCustoms(departureCustoms).withArrivalCustoms(arrivalCustoms)
                .withSender(sender1().withHandlingLocationCountryCode("AA").build())
                .withConsignee(consignee1().withHandlingLocationCountryCode("TR").build()).build();

        validator.validateCustoms(shipment);

        then(orderShipmentCustomsValidator).should(never()).validateDepartureFromTR(any(OrderShipmentDepartureCustoms.class));
        then(orderShipmentCustomsValidator).should(times(1)).validateDeparture(departureCustoms);
        then(orderShipmentCustomsValidator).should(times(1)).validateArrivalToTR(arrivalCustoms);
        then(orderShipmentCustomsValidator).should(never()).validateArrival(any(OrderShipmentArrivalCustoms.class));

    }

    @Test
    public void givenShipmentTREU_whenValidateCustoms_thenValidateDepartureTRAndArrivalEU() {
        OrderShipmentDepartureCustoms departureCustoms = OrderShipmentDepartureCustomsBuilder.anOrderShipmentDepartureCustoms().build();
        OrderShipmentArrivalCustoms arrivalCustoms = OrderShipmentArrivalCustomsBuilder.anOrderShipmentArrivalCustoms().build();
        OrderShipment shipment = newShipment1()
                .withDepartureCustoms(departureCustoms).withArrivalCustoms(arrivalCustoms)
                .withSender(sender1().withHandlingLocationCountryCode("TR").build())
                .withConsignee(consignee1().withHandlingLocationCountryCode("AA").build()).build();

        validator.validateCustoms(shipment);

        then(orderShipmentCustomsValidator).should(never()).validateDeparture(any(OrderShipmentDepartureCustoms.class));
        then(orderShipmentCustomsValidator).should(times(1)).validateDepartureFromTR(departureCustoms);
        then(orderShipmentCustomsValidator).should(times(1)).validateArrival(arrivalCustoms);
        then(orderShipmentCustomsValidator).should(never()).validateArrivalToTR(any(OrderShipmentArrivalCustoms.class));

    }

}