package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.order.domain.ShipmentHandlingParty;
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
public class OrderHandlingPartyValidatorUnitTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private KartoteksServiceClient kartoteksServiceClient;
    
    @MockBean
    private LocationServiceClient locationServiceClient;

    @MockBean
    private OrderHandlingPartyValidator validator;

    @Before
    public void init(){
        this.validator = new OrderHandlingPartyValidator(kartoteksServiceClient, locationServiceClient);

        given(kartoteksServiceClient.isCompanyExists(anyLong())).willAnswer(m -> (Long)m.getArguments()[0] > 0);
        given(kartoteksServiceClient.isLocationExists(anyLong())).willAnswer(m -> (Long)m.getArguments()[0] > 0);
        given(kartoteksServiceClient.isContactExists(anyLong())).willAnswer(m -> (Long)m.getArguments()[0] > 0);

    }

    private void validateConsigneeAndExpectException(ShipmentHandlingParty consignee, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validateConsignee(consignee);
    }
    private void validateSenderAndExpectException(ShipmentHandlingParty consignee, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validateSender(consignee);
    }

    /*
     handling party validations
    */
    @Test
    public void givenValidSender_whenValidate_thenThrowNoException() {
        validator.validateSender(sender1().build());
    }
    @Test
    public void givenValidConsignee_whenValidate_thenThrowNoException() {
        validator.validateConsignee(consignee1().build());
    }

    @Test
    public void givenShipmentWithEmptySender_whenValidate_thenThrowException() {
        validateSenderAndExpectException(null, "should have a sender");
    }
    @Test
    public void givenShipmentWithEmptySenderCompany_whenValidate_thenThrowException() {
        validateSenderAndExpectException(sender1().withCompany(null).build(), "should have a sender company");
    }
    @Test
    public void givenShipmentWithEmptySenderCompanyId_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withCompany(company100().withId(null).build()).build(),
                "should have a sender company");
    }
    @Test
    public void givenShipmentWithEmptySenderCompanyName_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withCompany(company100().withName("").build()).build(),
                "should have a sender company");
    }
    @Test
    public void givenShipmentWithInvalidSenderCompanyId_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withCompany(company100().withId(-1L).build()).build(),
                "Company 'Company 100' does not exist");
    }

    @Test
    public void givenShipmentWithEmptyConsignee_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(null, "should have a consignee");
    }
    @Test
    public void givenShipmentWithEmptyConsigneeCompany_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(consignee1().withCompany(null).build(),
                "should have a consignee company");
    }
    @Test
    public void givenShipmentWithEmptyConsigneeCompanyId_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withCompany(company100().withId(null).build()).build(),
                "should have a consignee company");
    }
    @Test
    public void givenShipmentWithEmptyConsigneeCompanyName_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withCompany(company100().withName("").build()).build(),
                "should have a consignee company");
    }
    @Test
    public void givenShipmentWithInvalidConsigneeCompanyId_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withCompany(company100().withId(-1L).build()).build(),
                "Company 'Company 100' does not exist");
    }

    @Test
    public void givenShipmentWithEmptySenderHandlingCompany_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withHandlingCompany(null).build(),
                "should have a sender handling company");
    }
    @Test
    public void givenShipmentWithEmptySenderHandlingCompanyId_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withHandlingCompany(company100().withId(null).build()).build(),
                "should have a sender handling company");
    }
    @Test
    public void givenShipmentWithEmptySenderHandlingCompanyName_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withHandlingCompany(company100().withName("").build()).build(),
                "should have a sender handling company");
    }
    @Test
    public void givenShipmentWithInvalidSenderHandlingCompanyId_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withHandlingCompany(company100().withId(-1L).build()).build(),
                "Company 'Company 100' does not exist");
    }

    @Test
    public void givenShipmentWithEmptyConsigneeHandlingCompany_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withHandlingCompany(null).build(),
                "should have a consignee handling company");
    }
    @Test
    public void givenShipmentWithEmptyConsigneeHandlingCompanyId_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withHandlingCompany(company100().withId(null).build()).build(),
                "should have a consignee handling company");
    }
    @Test
    public void givenShipmentWithEmptyConsigneeHandlingCompanyName_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withHandlingCompany(company100().withName("").build()).build(),
                "should have a consignee handling company");
    }
    @Test
    public void givenShipmentWithInvalidConsigneeHandlingCompanyId_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withHandlingCompany(company100().withId(-1L).build()).build(),
                "Company 'Company 100' does not exist");
    }

    @Test
    public void givenShipmentWithEmptySenderHandlingLocation_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withHandlingLocation(null).build(),
                "should have a sender handling location");
    }
    @Test
    public void givenShipmentWithEmptySenderHandlingLocationId_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withHandlingLocation(location200().withId(null).build()).build(),
                "should have a sender handling location");
    }
    @Test
    public void givenShipmentWithEmptySenderHandlingLocationName_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withHandlingLocation(location200().withName("").build()).build(),
                "should have a sender handling location");
    }
    @Test
    public void givenShipmentWithInvalidSenderHandlingLocationId_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withHandlingLocation(location200().withId(-1L).build()).build(),
                "Location 'Location 200' does not exist");
    }

    @Test
    public void givenShipmentWithEmptyConsigneeHandlingLocation_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withHandlingLocation(null).build(),
                "should have a consignee handling location");
    }
    @Test
    public void givenShipmentWithEmptyConsigneeHandlingLocationId_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withHandlingLocation(location200().withId(null).build()).build(),
                "should have a consignee handling location");
    }
    @Test
    public void givenShipmentWithEmptyConsigneeHandlingLocationName_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withHandlingLocation(location200().withName("").build()).build(),
                "should have a consignee handling location");
    }
    @Test
    public void givenShipmentWithInvalidConsigneeHandlingLocationId_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withHandlingLocation(location200().withId(-1L).build()).build(),
                "Location 'Location 200' does not exist");
    }

    @Test
    public void givenShipmentWithInvalidSenderCompanyContactId_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withCompanyContact(contact100().withId(-1L).build()).build(),
                "Contact 'Contact 100' does not exist");
    }

    @Test
    public void givenShipmentWithInvalidSenderHandlingContactId_whenValidate_thenThrowException() {
        validateSenderAndExpectException(
                sender1().withHandlingContact(contact100().withId(-1L).build()).build(),
                "Contact 'Contact 100' does not exist");
    }

    @Test
    public void givenShipmentWithInvalidConsigneeCompanyContactId_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withCompanyContact(contact100().withId(-1L).build()).build(),
                "Contact 'Contact 100' does not exist");
    }

    @Test
    public void givenShipmentWithInvalidConsigneeHandlingContactId_whenValidate_thenThrowException() {
        validateConsigneeAndExpectException(
                consignee1().withHandlingContact(contact100().withId(-1L).build()).build(),
                "Contact 'Contact 100' does not exist");
    }

}