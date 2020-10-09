package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.orders.order.builder.AppointmentBuilder;
import ekol.orders.order.builder.FixedZoneDateTimeBuilder;
import ekol.orders.order.domain.OrderShipment;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static ekol.orders.order.builder.MockOrderData.newShipment1;
import static ekol.orders.order.builder.MockOrderData.todayPlus2;
import static ekol.orders.order.builder.MockOrderData.todayUTC;

@RunWith(SpringRunner.class)
public class OrderShipmentDateValidatorUnitTest {

    private OrderShipmentDateValidator validator;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init(){
        this.validator = new OrderShipmentDateValidator();
    }

    private void expectValidationException(OrderShipment orderShipment, String expectedMessage){
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validate(orderShipment);
    }

    /*
     date validations
    */

    @Test
    public void givenValidShipment_whenValidate_thenThrowException() {
        validator.validate(newShipment1().build());
    }

    @Test
    public void givenShipmentWithEmptyReadyDateAndLoadingAppointment_whenValidate_thenThrowException() {
        expectValidationException(
                newShipment1().withReadyAtDate(null).withLoadingAppointment(null).build(),
                "should have a ready date or loading appointment");
    }
    @Test
    public void givenShipmentWithEmptyReadyDateAndLoadingAppointmentStart_whenValidate_thenThrowException() {
        expectValidationException(
                newShipment1().withReadyAtDate(null)
                        .withLoadingAppointment(AppointmentBuilder.anAppointment().build()).build(),
                "should have a ready date or loading appointment");
    }
    @Test
    public void givenShipmentWithReadyDateAndLoadingAppointmentStart_whenValidate_thenThrowException() {
        expectValidationException(newShipment1()
                        .withReadyAtDate(todayPlus2().build())
                        .withLoadingAppointment(AppointmentBuilder.anAppointment().withStartDateTime(todayPlus2().build()).build())
                                        .build(),
                "should have either a ready date or a loading appointment");
    }

    @Test
    public void givenShipmentWithDeliveryDateSmallerThanReadyDate_whenValidate_thenThrowException() {
        FixedZoneDateTime days2After = FixedZoneDateTimeBuilder.aFixedZoneDateTime()
                .withTimeZone("UTC").withDateTime(LocalDateTime.now().plusDays(2)).build();
        FixedZoneDateTime days3After = FixedZoneDateTimeBuilder.aFixedZoneDateTime()
                .withTimeZone("UTC").withDateTime(LocalDateTime.now().plusDays(3)).build();
        expectValidationException(
                newShipment1().withReadyAtDate(days3After).withDeliveryDate(days2After).build(),
                "ready date should be before delivery date");
    }
    @Test
    public void givenShipmentWithPastReadyDate_whenValidate_thenThrowException() {
        expectValidationException(
                newShipment1().withReadyAtDate(todayUTC().withDateTime(LocalDateTime.now().plusDays(-1)).build())
                                .build(),
                "should have a future ready date");
    }

    @Test
    public void givenShipmentWithReadyDateYearAfter_whenValidate_thenThrowException() {
        expectValidationException(
                newShipment1().withReadyAtDate(todayUTC().withDateTime(LocalDateTime.now().plusDays(1).plusYears(1)).build())
                        .build(),
                "should have a ready date within a year");
    }

    @Test
    public void givenShipmentWithPastLoadingAppointment_whenValidate_thenThrowException() {
        expectValidationException(
                newShipment1().withReadyAtDate(null)
                        .withLoadingAppointment(
                                AppointmentBuilder.anAppointment().withStartDateTime(
                                        todayUTC().withDateTime(LocalDateTime.now().plusDays(-1)).build()).build()
                                ).build(),
                "should have a future loading date appointment");
    }

    @Test
    public void givenShipmentWithLoadingAppointmentStartGreaterThanEnd_whenValidate_thenThrowException() {
        expectValidationException(newShipment1().withReadyAtDate(null)
                        .withLoadingAppointment(AppointmentBuilder.anAppointment()
                                        .withStartDateTime(FixedZoneDateTimeBuilder.aFixedZoneDateTime()
                                                        .withTimeZone("UTC").withDateTime(LocalDateTime.now().plusDays(2)).build()
                                        )
                                        .withEndDateTime(FixedZoneDateTimeBuilder.aFixedZoneDateTime()
                                                        .withTimeZone("UTC").withDateTime(LocalDateTime.now().plusDays(1)).build()
                                        ).build()
                        ).build(),
                "loading appointment should have an end date smaller than start date");
    }

    @Test
    public void givenShipmentWithLoadingAppointmentStartAfterYear_whenValidate_thenThrowException() {
        expectValidationException(newShipment1().withReadyAtDate(null)
                        .withLoadingAppointment(AppointmentBuilder.anAppointment()
                                .withStartDateTime(FixedZoneDateTimeBuilder.aFixedZoneDateTime()
                                        .withTimeZone("UTC").withDateTime(LocalDateTime.now().plusDays(1).plusYears(1)).build()
                                )
                                .build()
                        ).build(),
                "should have a loading appointment date within a year");
    }

    @Test
    public void givenShipmentWithUnloadingAppointmentStartGreaterThanEnd_whenValidate_thenThrowException() {
        expectValidationException(newShipment1().withUnloadingAppointment(
                AppointmentBuilder.anAppointment()
                        .withStartDateTime(FixedZoneDateTimeBuilder.aFixedZoneDateTime()
                                        .withTimeZone("UTC").withDateTime(LocalDateTime.now().plusDays(9)).build()
                        )
                        .withEndDateTime(FixedZoneDateTimeBuilder.aFixedZoneDateTime()
                                        .withTimeZone("UTC").withDateTime(LocalDateTime.now().plusDays(8)).build()
                        ).build()
                ).build(),
                "unloading appointment should have an end date smaller than start date");
    }
}