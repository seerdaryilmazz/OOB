package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.orders.lookup.builder.AdrClassDetailsBuilder;
import ekol.orders.lookup.builder.AdrPackageTypeBuilder;
import ekol.orders.lookup.repository.AdrClassDetailsRepository;
import ekol.orders.lookup.repository.AdrPackageTypeRepository;
import ekol.orders.order.domain.OrderShipmentAdr;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static ekol.orders.order.builder.MockOrderData.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class OrderShipmentAdrValidatorUnitTest {

    private OrderShipmentAdrValidator validator;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private AdrPackageTypeRepository adrPackageTypeRepository;

    @MockBean
    private AdrClassDetailsRepository adrClassDetailsRepository;

    @Before
    public void init(){
        this.validator = new OrderShipmentAdrValidator(adrClassDetailsRepository, adrPackageTypeRepository);

        given(adrClassDetailsRepository.findById(1L)).willReturn(Optional.of(adrClassA().build()));
        given(adrClassDetailsRepository.findById(2L)).willReturn(Optional.empty());

        given(adrPackageTypeRepository.findById(1L)).willReturn(Optional.of(adrPackageTypePallet().build()));
        given(adrPackageTypeRepository.findById(2L)).willReturn(Optional.of(adrPackageTypeBox().build()));
        given(adrPackageTypeRepository.findById(3L)).willReturn(Optional.empty());
    }

    private void callValidateAndExpectException(OrderShipmentAdr orderShipmentAdr, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validate(orderShipmentAdr);

    }

    @Test
    public void givenValidShipmentAdr_whenValidate_thenThrowNoException(){
        validator.validate(adrDetails1().build());
    }

    @Test
    public void givenShipmentAdrWithEmptyAdrDetails_whenValidate_thenThrowException() {
        callValidateAndExpectException(adrDetails1().withAdrClassDetails(null).build(),
                "shipment should have ADR class details");
    }
    @Test
    public void givenShipmentAdrWithEmptyAdrDetailsId_whenValidate_thenThrowException() {
        callValidateAndExpectException(
                adrDetails1().withAdrClassDetails(AdrClassDetailsBuilder.anAdrClassDetails().withId(null).build()).build(),
                "shipment should have ADR class details");
    }

    @Test
    public void givenShipmentAdrWithInvalidAdrDetailsId_whenValidate_thenThrowException() {
        callValidateAndExpectException(
                adrDetails1().withAdrClassDetails(AdrClassDetailsBuilder.anAdrClassDetails().withId(2L).build()).build(),
                "ADR class detail with id 2 does not exist");
    }

    @Test
    public void givenShipmentAdrWithEmptyQuantity_whenValidate_thenThrowException() {
        callValidateAndExpectException(
                adrDetails1().withQuantity(null).build(),
                "ADR details should have a quantity");
    }
    @Test
    public void givenShipmentAdrWithZeroQuantity_whenValidate_thenThrowException() {
        callValidateAndExpectException(
                adrDetails1().withQuantity(0).build(),
                "ADR details should have a quantity");
    }

    @Test
    public void givenShipmentAdrWithEmptyPackageTypeId_whenValidate_thenThrowException() {
        callValidateAndExpectException(
                adrDetails1().withPackageType(null).build(),
                "ADR details should have a package type");
    }
    @Test
    public void givenShipmentAdrWithInvalidPackageTypeId_whenValidate_thenThrowException() {
        callValidateAndExpectException(
                adrDetails1().withPackageType(AdrPackageTypeBuilder.anAdrPackageType().withId(3L).build()).build(),
                "ADR package type with id 3 does not exist");
    }
    @Test
    public void givenShipmentAdrWithEmptyAmount_whenValidate_thenThrowException() {
        callValidateAndExpectException(
                adrDetails1().withAmount(null).build(),
                "ADR details should have an amount");
    }

    @Test
    public void givenShipmentAdrWithZeroAmount_whenValidate_thenThrowException() {
        callValidateAndExpectException(
                adrDetails1().withAmount(new BigDecimal(0)).build(),
                "ADR details should have an amount");
    }

    @Test
    public void givenShipmentAdrWithEmptyAmountUnit_whenValidate_thenThrowException() {
        callValidateAndExpectException(
                adrDetails1().withUnit(null).build(),
                "ADR details should have an amount unit");
    }


    @Test
    public void givenShipmentAdrWithInvalidInnerPackageTypeId_whenValidate_thenThrowException() {
        callValidateAndExpectException(
                adrDetails1().withInnerPackageType(AdrPackageTypeBuilder.anAdrPackageType().withId(3L).build()).build(),
                "ADR inner package type with id 3 does not exist");
    }


}
