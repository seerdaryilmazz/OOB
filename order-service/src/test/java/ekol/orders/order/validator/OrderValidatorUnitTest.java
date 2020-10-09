package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.order.domain.Document;
import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.Status;
import ekol.orders.order.service.KartoteksServiceClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

import static ekol.orders.order.builder.MockOrderData.*;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class OrderValidatorUnitTest {

    private OrderValidator validator;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private KartoteksServiceClient kartoteksServiceClient;

    @MockBean
    private OrderShipmentValidator orderShipmentValidator;

    @MockBean
    private DocumentValidator documentValidator;

    @Before
    public void init(){
        this.validator = new OrderValidator(kartoteksServiceClient, orderShipmentValidator, documentValidator);

        given(kartoteksServiceClient.isCompanyExists(anyLong())).willAnswer(m -> (Long)m.getArguments()[0] > 0);
        given(kartoteksServiceClient.isLocationExists(anyLong())).willAnswer(m -> (Long)m.getArguments()[0] > 0);
        given(kartoteksServiceClient.isContactExists(anyLong())).willAnswer(m -> (Long)m.getArguments()[0] > 0);

    }

    @Test
    public void givenValidOrder_whenValidateNew_thenThrowNoException(){
        Order order = validNewOrder().build();

        validator.validateNew(order);

        then(orderShipmentValidator).should(times(1)).validate(order.getShipments().get(0));
    }

    @Test
    public void givenValidOrderWithOnlyRequiredData_whenValidateNew_thenThrowNoException(){
        Order order = validNewOrderOnlyRequired().build();

        validator.validateNew(order);

        then(orderShipmentValidator).should(times(1)).validate(order.getShipments().get(0));
    }

    @Test
    public void givenValidOrderWithDocuments_whenValidateDocuments_thenThrowNoException(){
        Order order = validNewOrder().build();

        validator.validateDocuments(order);

        then(orderShipmentValidator).should(times(1)).validateDocuments(order.getShipments().get(0));
        then(documentValidator).should(times(1)).validate(anyListOf(Document.class));
    }

    private void expectValidationException(Order order, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validateNew(order);
    }

    @Test
    public void givenOrderWithID_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withId(1L).build(), "should not have an ID");
    }

    @Test
    public void givenOrderWithEmptyServiceType_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withServiceType(null).build(), "should have a service type");
    }
    @Test
    public void givenOrderWithEmptyTruckLoadType_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withTruckLoadType(null).build(), "should have a truck load type");
    }

    @Test
    public void givenOrderWithEmptyStatus_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withStatus(null).build(), "should have a status");
    }
    @Test
    public void givenOrderWithInvalidStatus_whenValidateNew_thenThrowException() {
        Arrays.stream(Status.values())
                .filter(status -> !(status.equals(Status.CREATED) || status.equals(Status.CONFIRMED)))
                .forEach(status -> {
                    try{
                        validator.validateNew(validNewOrder().withStatus(status).build());
                        fail("Order status should not be validated");
                    }catch(ValidationException e){
                        assertThat(e.getMessage(), containsString("order should not have " + status.name() + " status"));
                    }
                });

    }
    @Test
    public void givenOrderWithEmptyShipments_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withShipments(Collections.emptyList()).build(),
                "should have at least one shipment");
    }

    /*
     subsidiary validations
    */

    @Test
    public void givenOrderWithEmptySubsidiary_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withSubsidiary(null).build(), "should have a subsidiary");
    }
    @Test
    public void givenOrderWithEmptySubsidiaryId_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withSubsidiary(subsidiaryTR().withId(null).build()).build(),
                "should have a subsidiary");
    }
    @Test
    public void givenOrderWithEmptySubsidiaryName_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withSubsidiary(subsidiaryTR().withName("").build()).build(),
                "should have a subsidiary");
    }

    /*
     customer validations
    */

    @Test
    public void givenOrderWithEmptyCustomer_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withCustomer(null).build(), "should have a customer");
    }
    @Test
    public void givenOrderWithEmptyCustomerId_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withCustomer(customer1().withId(null).build()).build(),
                "should have a customer");
    }
    @Test
    public void givenOrderWithEmptyCustomerName_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withCustomer(customer1().withName("").build()).build(),
                "should have a customer");
    }
    @Test
    public void givenOrderWithInvalidCustomerId_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withCustomer(customer1().withId(-1L).build()).build(),
                "Customer with id -1 does not exist");
    }
    @Test
    public void givenOrderWithInvalidOriginalCustomerId_whenValidateNew_thenThrowException() {
        expectValidationException(validNewOrder().withOriginalCustomer(customer2().withId(-1L).build()).build(),
                "Original customer with id -1 does not exist");
    }

    @Test
    public void givenOrderWithMultipleShipments_whenValidateMeasurements_thenCallValidateTotalMeasurementsForEachShipment(){
        Order order = validNewOrder().withShipments(
                Arrays.asList(newShipment1().build(), newShipment1().build())
        ).build();

        validator.validateMeasurements(order);

        order.getShipments().forEach(orderShipment -> {
            then(orderShipmentValidator).should(times(1)).validateTotalMeasurements(orderShipment);
        });
    }

    @Test
    public void givenOrderWithMultipleShipments_whenValidateLoadSpecs_thenCallValidateLoadSpecsForEachShipment(){
        Order order = validNewOrder().withShipments(
                Arrays.asList(newShipment1().build(), newShipment1().build())
        ).build();

        validator.validateLoadSpecs(order);

        order.getShipments().forEach(orderShipment -> {
            then(orderShipmentValidator).should(times(1)).validateLoadSpecs(orderShipment);
        });
    }

}
