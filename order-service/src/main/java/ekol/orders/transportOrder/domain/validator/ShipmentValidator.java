package ekol.orders.transportOrder.domain.validator;

import ekol.exceptions.ValidationException;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.orders.transportOrder.domain.Shipment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created by burak on 06/10/17.
 */
@Component
public class ShipmentValidator {

    public void validate(Shipment shipment) {

        this.validateDates(shipment);

    }

    public void validateDates(Shipment shipment) {

        FixedZoneDateTime readyDate = shipment.getReadyAtDate();

        if (readyDate == null) {
            throw new ValidationException("Ready Date can not be empty.");
        }

        if (shipment.getPickupAppointment() != null) {

            LocalDateTime startDateWithoutTime = shipment.getPickupAppointment().getStart();
            LocalDateTime endDateWithoutTime = shipment.getPickupAppointment().getEnd();

            if (startDateWithoutTime == null && endDateWithoutTime == null) {
                throw new ValidationException("Invalid 'Pick Up Appointment.'");
            } else if (shipment.getPickupAppointment().getTimeZone() == null) {
                throw new ValidationException("'Pick Up Appointment Time Zone' can not be empty.");
            } else if (!readyDate.getTimeZone().contentEquals(shipment.getPickupAppointment().getTimeZone())) {
                throw new ValidationException("'Ready Date' and 'Pick Up Appointments' have different time zones.");
            } else {
                if (startDateWithoutTime != null) {
                    if (readyDate.getDateTime().toLocalDate().isAfter(startDateWithoutTime.toLocalDate())) {
                        throw new ValidationException("'Pick Up Appointment Start Day' should be on the same day or later with 'Requested Pick Up Date'.");
                    }
                    if (endDateWithoutTime != null && startDateWithoutTime.toLocalDate().isAfter(endDateWithoutTime.toLocalDate())) {
                        throw new ValidationException("'Pick Up End Date' should be after 'Pick Up Start Date'");
                    }
                } else if (endDateWithoutTime != null && readyDate.getDateTime().toLocalDate().isAfter(endDateWithoutTime.toLocalDate())) {
                    throw new ValidationException("'Pick Up Appointment End Day' should be on the same day or later with 'Requested Pick Up Date'.");
                }
            }
        }


        FixedZoneDateTime requestedDeliveryDate = shipment.getRequestedDeliveryDate();

            if (shipment.getDeliveryAppointment() != null) {

                LocalDateTime startDateWithoutTime = shipment.getDeliveryAppointment().getStart();
                LocalDateTime endDateWithoutTime = shipment.getDeliveryAppointment().getEnd();

                if (startDateWithoutTime == null && endDateWithoutTime == null) {
                    throw new ValidationException("Invalid Delivery Appointment.");
                } else if (shipment.getDeliveryAppointment().getTimeZone() == null) {
                    throw new ValidationException("Delivery Appointment time zone can not be empty.");
                } else if (requestedDeliveryDate != null && !requestedDeliveryDate.getTimeZone().contentEquals(shipment.getDeliveryAppointment().getTimeZone())) {
                    throw new ValidationException("Requested Delivery Date and Delivery Appointments have different time zones.");
                } else {
                    if (startDateWithoutTime != null) {
                        if (requestedDeliveryDate != null && requestedDeliveryDate.getDateTime().toLocalDate().isAfter(startDateWithoutTime.toLocalDate())) {
                            throw new ValidationException("'Delivery Appointment Start Day' should be on the same day or later with 'Requested Delivery Date'.");
                        }
                        if (endDateWithoutTime != null && startDateWithoutTime.toLocalDate().isAfter(endDateWithoutTime.toLocalDate())) {
                            throw new ValidationException("'Delivery End Date' should be after 'Delivery Start Date'");
                        }
                    } else if (requestedDeliveryDate != null && endDateWithoutTime != null && requestedDeliveryDate.getDateTime().toLocalDate().isAfter(endDateWithoutTime.toLocalDate())) {
                        throw new ValidationException("'Delivery Appointment End Day' should be on the same day or later with 'Requested Delivery Date'.");
                    }
                }
            }
        }

}
