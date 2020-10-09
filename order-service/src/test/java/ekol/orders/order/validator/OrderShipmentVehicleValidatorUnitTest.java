package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.order.domain.OrderShipment;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static ekol.orders.order.builder.MockOrderData.boxRequirement;
import static ekol.orders.order.builder.MockOrderData.newShipment1;

@RunWith(SpringRunner.class)
public class OrderShipmentVehicleValidatorUnitTest {
    private OrderShipmentVehicleValidator validator;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void init(){
        this.validator = new OrderShipmentVehicleValidator();

    }

    private void callValidateAndExpectException(OrderShipment orderShipment, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validate(orderShipment);

    }

    @Test
    public void givenShipmentWithEmptyVehicleFeature_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(
                newShipment1().withVehicleRequirements(
                        Collections.singletonList(boxRequirement().withRequirement(null).build())
                ).build(),
                "shipment vehicle requirement should have vehicle feature");
    }
    @Test
    public void givenShipmentWithEmptyOperationType_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(
                newShipment1().withVehicleRequirements(
                        Collections.singletonList(boxRequirement().withOperationType(null).build())
                ).build(),
                "shipment vehicle requirement should have operation type");
    }
}
