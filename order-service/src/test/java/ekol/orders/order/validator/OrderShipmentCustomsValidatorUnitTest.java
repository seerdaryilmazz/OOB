package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.order.domain.CodeNameEmbeddable;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.OrderShipmentArrivalCustoms;
import ekol.orders.order.domain.OrderShipmentDepartureCustoms;
import ekol.orders.order.service.KartoteksServiceClient;
import ekol.orders.order.service.LocationServiceClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static ekol.orders.order.builder.MockOrderData.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;

@RunWith(SpringRunner.class)
public class OrderShipmentCustomsValidatorUnitTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private KartoteksServiceClient kartoteksServiceClient;

    @MockBean
    private LocationServiceClient locationServiceClient;

    @MockBean
    private OrderShipmentCustomsValidator validator;

    @Before
    public void init(){
        this.validator = new OrderShipmentCustomsValidator(locationServiceClient, kartoteksServiceClient);

        given(kartoteksServiceClient.isCompanyExists(anyLong())).willAnswer(m -> (Long)m.getArguments()[0] > 0);
        given(kartoteksServiceClient.isLocationExists(anyLong())).willAnswer(m -> (Long)m.getArguments()[0] > 0);

        given(locationServiceClient.isCustomsOfficeExists(anyLong())).willAnswer(m -> (Long)m.getArguments()[0] > 0);
        given(locationServiceClient.isCustomsLocationExists(anyLong())).willAnswer(m -> (Long)m.getArguments()[0] > 0);

    }

    private void callValidateDepartureAndExpectException(OrderShipmentDepartureCustoms customs, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validateDeparture(customs);

    }

    private void callValidateArrivalAndExpectException(OrderShipmentArrivalCustoms customs, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validateArrival(customs);

    }

    private void callValidateArrivalTRAndExpectException(OrderShipmentArrivalCustoms customs, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validateArrivalToTR(customs);

    }

    private void callValidateDepartureTRAndExpectException(OrderShipmentDepartureCustoms customs, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validateDepartureFromTR(customs);

    }

    @Test
    public void givenValidDepartureEUCustoms_whenValidateDeparture_thenThrowNoException() {
        validator.validateDeparture(customsDepartureEU().build());
    }

    @Test
    public void givenDepartureEUCustomsWithEmptyCustomsAgent_whenValidateDeparture_thenThrowException() {
        callValidateDepartureAndExpectException(customsDepartureEU().withCustomsAgent(null).build(), "should have a departure customs agent");
    }

    @Test
    public void givenDepartureEUCustomsWithEmptyCustomsAgentId_whenValidateDeparture_thenThrowException() {
        callValidateDepartureAndExpectException(
                customsDepartureEU().withCustomsAgent(IdNameEmbeddable.with(null, "customs agent")).build(),
                "should have a departure customs agent");
    }

    @Test
    public void givenDepartureEUCustomsWithEmptyCustomsAgentName_whenValidateDeparture_thenThrowException() {
        callValidateDepartureAndExpectException(
                customsDepartureEU().withCustomsAgent(IdNameEmbeddable.with(1L, "")).build(),
                "should have a departure customs agent");
    }

    @Test
    public void givenDepartureEUCustomsWithEmptyCustomsAgentLocation_whenValidateDeparture_thenThrowException() {
        callValidateDepartureAndExpectException(
                customsDepartureEU().withCustomsAgentLocation(null).build(),
                "should have a departure customs agent location");
    }

    @Test
    public void givenDepartureEUCustomsWithEmptyCustomsAgentLocationId_whenValidateDeparture_thenThrowException() {
        callValidateDepartureAndExpectException(
                customsDepartureEU().withCustomsAgentLocation(IdNameEmbeddable.with(null, "location")).build(),
                "should have a departure customs agent location");
    }

    @Test
    public void givenDepartureEUCustomsWithEmptyCustomsAgentLocationName_whenValidateDeparture_thenThrowException() {
        callValidateDepartureAndExpectException(
                customsDepartureEU().withCustomsAgentLocation(IdNameEmbeddable.with(1L, "")).build(),
                "should have a departure customs agent location");
    }

    @Test
    public void givenDepartureEUCustomsWithInvalidAgent_whenValidateDeparture_thenThrowException() {
        callValidateDepartureAndExpectException(
                customsDepartureEU().withCustomsAgent(IdNameEmbeddable.with(-1L, "agent")).build(),
                "agent 'agent' does not exist");
    }

    @Test
    public void givenDepartureEUCustomsWithInvalidAgentLocation_whenValidateDeparture_thenThrowException() {
        callValidateDepartureAndExpectException(
                customsDepartureEU().withCustomsAgentLocation(IdNameEmbeddable.with(-1L, "location")).build(),
                "agent location 'location' does not exist");
    }

    @Test
    public void givenValidArrivalEUCustoms_whenValidateArrival_thenThrowNoException() {
        validator.validateArrival(customsArrivalEU().build());
    }

    @Test
    public void givenArrivalEUCustomsWithEmptyCustomsAgent_whenValidateArrival_thenThrowException() {
        callValidateArrivalAndExpectException(customsArrivalEU().withCustomsAgent(null).build(),
                "should have an arrival customs agent");
    }

    @Test
    public void givenArrivalEUCustomsWithEmptyCustomsAgentId_whenValidateArrival_thenThrowException() {
        callValidateArrivalAndExpectException(
                customsArrivalEU().withCustomsAgent(IdNameEmbeddable.with(null, "customs agent")).build(),
                "should have an arrival customs agent");
    }

    @Test
    public void givenArrivalEUCustomsWithEmptyCustomsAgentName_whenValidateArrival_thenThrowException() {
        callValidateArrivalAndExpectException(
                customsArrivalEU().withCustomsAgent(IdNameEmbeddable.with(1L, "")).build(),
                "should have an arrival customs agent");
    }

    @Test
    public void givenArrivalEUCustomsWithEmptyCustomsAgentLocation_whenValidateArrival_thenThrowException() {
        callValidateArrivalAndExpectException(
                customsArrivalEU().withCustomsAgentLocation(null).build(),
                "should have an arrival customs agent location");
    }

    @Test
    public void givenArrivalEUCustomsWithEmptyCustomsAgentLocationId_whenValidateArrival_thenThrowException() {
        callValidateArrivalAndExpectException(
                customsArrivalEU().withCustomsAgentLocation(IdNameEmbeddable.with(null, "location")).build(),
                "should have an arrival customs agent location");
    }

    @Test
    public void givenArrivalEUCustomsWithEmptyCustomsAgentLocationName_whenValidateArrival_thenThrowException() {
        callValidateArrivalAndExpectException(
                customsArrivalEU().withCustomsAgentLocation(IdNameEmbeddable.with(1L, "")).build(),
                "should have an arrival customs agent location");
    }

    @Test
    public void givenArrivalEUCustomsWithInvalidAgent_whenValidateArrival_thenThrowException() {
        callValidateArrivalAndExpectException(
                customsArrivalEU().withCustomsAgent(IdNameEmbeddable.with(-1L, "agent")).build(),
                "agent 'agent' does not exist");
    }

    @Test
    public void givenArrivalEUCustomsWithInvalidAgentLocation_whenValidateArrival_thenThrowException() {
        callValidateArrivalAndExpectException(
                customsArrivalEU().withCustomsAgentLocation(IdNameEmbeddable.with(-1L, "location")).build(),
                "agent location 'location' does not exist");
    }

    @Test
    public void givenValidArrivalTRCustoms_whenValidateArrivalTR_thenThrowNoException() {
        validator.validateArrivalToTR(customsArrivalTR().build());
    }

    @Test
    public void givenValidArrivalFreeZoneTRCustoms_whenValidateArrivalTR_thenThrowNoException() {
        validator.validateArrivalToTR(
                customsArrivalTR()
                        .withCustomsType(CodeNameEmbeddable.with("FREE_ZONE", "Free Zone"))
                        .withCustomsLocation(null)
                        .build());
    }

    @Test
    public void givenArrivalTRCustomsWithEmptyCustomsOffice_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(customsArrivalTR().withCustomsOffice(null).build(),
                "should have an arrival customs office");
    }

    @Test
    public void givenArrivalTRCustomsWithEmptyCustomsOfficeId_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(
                customsArrivalTR().withCustomsOffice(IdNameEmbeddable.with(null, "customs office")).build(),
                "should have an arrival customs office");
    }

    @Test
    public void givenArrivalTRCustomsWithEmptyCustomsOfficeName_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(
                customsArrivalTR().withCustomsOffice(IdNameEmbeddable.with(1L, "")).build(),
                "should have an arrival customs office");
    }

    @Test
    public void givenArrivalTRCustomsWithEmptyCustomsLocation_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(customsArrivalTR().withCustomsLocation(null).build(),
                "should have an arrival customs location");
    }

    @Test
    public void givenArrivalTRCustomsWithEmptyCustomsLocationId_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(
                customsArrivalTR().withCustomsLocation(IdNameEmbeddable.with(null, "customs location")).build(),
                "should have an arrival customs location");
    }

    @Test
    public void givenArrivalTRCustomsWithEmptyCustomsLocationName_whenValidateDepartureTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(
                customsArrivalTR().withCustomsLocation(IdNameEmbeddable.with(1L, "")).build(),
                "should have an arrival customs location");
    }

    @Test
    public void givenArrivalTRCustomsWithEmptyCustomsType_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(customsArrivalTR().withCustomsType(null).build(),
                "should have an arrival customs type");
    }

    @Test
    public void givenArrivalTRCustomsWithEmptyCustomsTypeCode_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(
                customsArrivalTR().withCustomsType(CodeNameEmbeddable.with(null, "type")).build(),
                "should have an arrival customs type");
    }

    @Test
    public void givenArrivalTRCustomsWithEmptyCustomsTypeName_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(
                customsArrivalTR().withCustomsType(CodeNameEmbeddable.with("TYPE", "")).build(),
                "should have an arrival customs type");
    }

    @Test
    public void givenArrivalTRCustomsWithEmptyCustomsAgent_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(customsArrivalTR().withCustomsAgent(null).build(),
                "should have an arrival customs agent");
    }

    @Test
    public void givenArrivalTRCustomsWithEmptyCustomsAgentId_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(
                customsArrivalTR().withCustomsAgent(IdNameEmbeddable.with(null, "customs agent")).build(),
                "should have an arrival customs agent");
    }

    @Test
    public void givenArrivalTRCustomsWithEmptyCustomsAgentName_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(
                customsArrivalTR().withCustomsAgent(IdNameEmbeddable.with(1L, "")).build(),
                "should have an arrival customs agent");
    }

    @Test
    public void givenArrivalTRCustomsWithInvalidAgent_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(
                customsArrivalTR().withCustomsAgent(IdNameEmbeddable.with(-1L, "agent")).build(),
                "agent 'agent' does not exist");
    }

    @Test
    public void givenArrivalTRCustomsWithInvalidCustomsOffice_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(
                customsArrivalTR().withCustomsOffice(IdNameEmbeddable.with(-1L, "office")).build(),
                "customs office 'office' does not exist");
    }

    @Test
    public void givenArrivalTRCustomsWithInvalidCustomsLocation_whenValidateArrivalTR_thenThrowException() {
        callValidateArrivalTRAndExpectException(
                customsArrivalTR().withCustomsLocation(IdNameEmbeddable.with(-1L, "location")).build(),
                "customs location 'location' does not exist");
    }

    @Test
    public void givenValidDepartureTRCustoms_whenValidateDepartureTR_thenThrowNoException() {
        validator.validateDepartureFromTR(customsDepartureTR().build());
    }

    @Test
    public void givenDepartureTRCustomsWithEmptyCustomsOffice_whenValidateDepartureTR_thenThrowException() {
        callValidateDepartureTRAndExpectException(customsDepartureTR().withCustomsOffice(null).build(),
                "should have a departure customs office");
    }

    @Test
    public void givenDepartureTRCustomsWithEmptyCustomsOfficeId_whenValidateDepartureTR_thenThrowException() {
        callValidateDepartureTRAndExpectException(
                customsDepartureTR().withCustomsOffice(IdNameEmbeddable.with(null, "customs office")).build(),
                "should have a departure customs office");
    }

    @Test
    public void givenDepartureTRCustomsWithEmptyCustomsOfficeName_whenValidateDepartureTR_thenThrowException() {
        callValidateDepartureTRAndExpectException(
                customsDepartureTR().withCustomsOffice(IdNameEmbeddable.with(1L, "")).build(),
                "should have a departure customs office");
    }

    @Test
    public void givenDepartureTRCustomsWithEmptyCustomsAgent_whenValidateDepartureTR_thenThrowException() {
        callValidateDepartureTRAndExpectException(customsDepartureTR().withCustomsAgent(null).build(),
                "should have a departure customs agent");
    }

    @Test
    public void givenDepartureTRCustomsWithEmptyCustomsAgentId_whenValidateDepartureTR_thenThrowException() {
        callValidateDepartureTRAndExpectException(
                customsDepartureTR().withCustomsAgent(IdNameEmbeddable.with(null, "customs agent")).build(),
                "should have a departure customs agent");
    }

    @Test
    public void givenDepartureTRCustomsWithEmptyCustomsAgentName_whenValidateDepartureTR_thenThrowException() {
        callValidateDepartureTRAndExpectException(
                customsDepartureTR().withCustomsAgent(IdNameEmbeddable.with(1L, "")).build(),
                "should have a departure customs agent");
    }

    @Test
    public void givenDepartureTRCustomsWithInvalidCustomsOffice_whenValidateDepartureTR_thenThrowException() {
        callValidateDepartureTRAndExpectException(
                customsDepartureTR().withCustomsOffice(IdNameEmbeddable.with(-1L, "office")).build(),
                "customs office 'office' does not exist");
    }

    @Test
    public void givenDepartureTRCustomsWithInvalidCustomsAgent_whenValidateDepartureTR_thenThrowException() {
        callValidateDepartureTRAndExpectException(
                customsDepartureTR().withCustomsAgent(IdNameEmbeddable.with(-1L, "agent")).build(),
                "customs agent 'agent' does not exist");
    }


}
