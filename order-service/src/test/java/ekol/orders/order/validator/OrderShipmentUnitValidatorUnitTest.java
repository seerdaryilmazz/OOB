package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.hibernate5.domain.embeddable.BigDecimalRange;
import ekol.orders.builder.BigDecimalRangeBuilder;
import ekol.orders.lookup.builder.PackageTypeBuilder;
import ekol.orders.builder.PackageTypeRestrictionBuilder;
import ekol.orders.order.domain.OrderShipmentUnit;
import ekol.orders.transportOrder.repository.PackageTypeRestrictionRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static ekol.orders.order.builder.MockOrderData.pallet1;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class OrderShipmentUnitValidatorUnitTest {
    private OrderShipmentUnitValidator validator;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private PackageTypeRestrictionRepository packageTypeRestrictionRepository;

    @Before
    public void init(){
        this.validator = new OrderShipmentUnitValidator(packageTypeRestrictionRepository);

        BigDecimalRange between10And100 = BigDecimalRangeBuilder.aBigDecimalRange().withMinValue(new BigDecimal(10)).withMaxValue(new BigDecimal(100)).build();
        BigDecimalRange between20And110 = BigDecimalRangeBuilder.aBigDecimalRange().withMinValue(new BigDecimal(20)).withMaxValue(new BigDecimal(110)).build();
        BigDecimalRange between30And120 = BigDecimalRangeBuilder.aBigDecimalRange().withMinValue(new BigDecimal(30)).withMaxValue(new BigDecimal(120)).build();
        given(packageTypeRestrictionRepository.findByPackageTypeId(1L)).willReturn(
                Optional.of(PackageTypeRestrictionBuilder.aPackageTypeRestriction()
                        .withWidthRangeInCentimeters(between10And100)
                        .withLengthRangeInCentimeters(between20And110)
                        .withHeightRangeInCentimeters(between30And120).build()));

        given(packageTypeRestrictionRepository.findByPackageTypeId(2L)).willReturn(Optional.empty());
    }

    private void expectValidationException(OrderShipmentUnit shipmentUnit, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validate(shipmentUnit);
    }

    /*
     shipment unit validations
    */

    @Test
    public void givenValidShipmentUnit_whenValidate_thenThrowException() {

        validator.validate(pallet1().build());
    }

    @Test
    public void givenShipmentUnitWithEmptyQuantity_whenValidate_thenThrowException() {
        expectValidationException(
                pallet1().withQuantity(null).build(),
                "shipment unit should have a quantity");
    }
    @Test
    public void givenShipmentUnitWithZeroQuantity_whenValidate_thenThrowException() {
        expectValidationException(
                pallet1().withQuantity(0).build(),
                "shipment unit should have a quantity");
    }

    @Test
    public void givenShipmentUnitWithEmptyPackageType_whenValidate_thenThrowException() {
        expectValidationException(
                pallet1().withPackageType(null).build(),
                "shipment unit should have a package type");
    }
    @Test
    public void givenShipmentUnitWithEmptyPackageTypeId_whenValidate_thenThrowException() {
        expectValidationException(
                pallet1().withPackageType(PackageTypeBuilder.aPackageType().withId(null).build()).build(),
                "shipment unit should have a package type");
    }

    @Test
    public void givenShipmentUnitWithWidthGreaterThanLength_whenValidate_thenThrowException() {
        expectValidationException(
                pallet1().withWidth(new BigDecimal(60)).withLength(new BigDecimal(50)).build(),
                "shipment unit should have a width smaller than or equal to length");
    }

    @Test
    public void givenShipmentUnitWithWidthSmallerThanRestriction_whenValidate_thenThrowException() {
        expectValidationException(
                pallet1().withWidth(new BigDecimal(5)).build(),
                "shipment unit should have a width in range 10, 100");
    }

    @Test
    public void givenShipmentUnitWithWidthGreaterThanRestriction_whenValidate_thenThrowException() {
        expectValidationException(
                pallet1().withWidth(new BigDecimal(105)).withLength(new BigDecimal(105)).build(),
                "shipment unit should have a width in range 10, 100");
    }

    @Test
    public void givenShipmentUnitWithLengthSmallerThanRestriction_whenValidate_thenThrowException() {
        expectValidationException(
                pallet1().withLength(new BigDecimal(15)).withWidth(new BigDecimal(11)).build(),
                "shipment unit should have a length in range 20, 110");
    }

    @Test
    public void givenShipmentUnitWithLengthGreaterThanRestriction_whenValidate_thenThrowException() {
        expectValidationException(
                pallet1().withLength(new BigDecimal(115)).build(),
                "shipment unit should have a length in range 20, 110");
    }

    @Test
    public void givenShipmentUnitWithHeightSmallerThanRestriction_whenValidate_thenThrowException() {
        expectValidationException(
                pallet1().withHeight(new BigDecimal(25)).build(),
                "shipment unit should have a height in range 30, 120");
    }

    @Test
    public void givenShipmentUnitWithHeightGreaterThanRestriction_whenValidate_thenThrowException() {
        expectValidationException(
                pallet1().withHeight(new BigDecimal(125)).build(),
                "shipment unit should have a height in range 30, 120");
    }
}