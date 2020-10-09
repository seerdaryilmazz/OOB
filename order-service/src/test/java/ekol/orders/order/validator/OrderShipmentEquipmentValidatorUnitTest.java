package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.transportOrder.repository.EquipmentTypeRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static ekol.orders.order.builder.MockOrderData.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class OrderShipmentEquipmentValidatorUnitTest {
    private OrderShipmentEquipmentValidator validator;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private EquipmentTypeRepository equipmentTypeRepository;

    @Before
    public void init(){
        this.validator = new OrderShipmentEquipmentValidator(equipmentTypeRepository);

        given(equipmentTypeRepository.findById(1L)).willReturn(Optional.of(equipment().build()));
        given(equipmentTypeRepository.findById(2L)).willReturn(Optional.empty());

    }

    private void callValidateAndExpectException(OrderShipment orderShipment, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validate(orderShipment);

    }

    @Test
    public void givenShipmentWithEmptyEquipmentType_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(
                newShipment1().withEquipmentRequirements(
                        Collections.singletonList(equipmentRequirement().withEquipment(null).build())
                ).build(),
                "shipment equipment requirement should have equipment type");
    }
    @Test
    public void givenShipmentWithEmptyEquipmentTypeId_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(
                newShipment1().withEquipmentRequirements(
                        Collections.singletonList(equipmentRequirement().withEquipment(equipment().withId(null).build()).build())
                ).build(),
                "shipment equipment requirement should have equipment type");
    }
    @Test
    public void givenShipmentWithInvalidEquipmentTypeId_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(
                newShipment1().withEquipmentRequirements(
                        Collections.singletonList(equipmentRequirement().withEquipment(equipment().withId(2L).build()).build())
                ).build(),
                "Equipment type with id 2 does not exist");
    }
    @Test
    public void givenShipmentWithEmptyCount_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(
                newShipment1().withEquipmentRequirements(
                        Collections.singletonList(equipmentRequirement().withCount(null).build())
                ).build(),
                "shipment equipment requirement should have count");
    }
    @Test
    public void givenShipmentWithInvalidCount_whenValidateNew_thenThrowException() {
        callValidateAndExpectException(
                newShipment1().withEquipmentRequirements(
                        Collections.singletonList(equipmentRequirement().withCount(-2).build())
                ).build(),
                "shipment equipment requirement should have count");
    }
}
