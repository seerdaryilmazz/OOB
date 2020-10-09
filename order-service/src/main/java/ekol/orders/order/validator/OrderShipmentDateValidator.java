package ekol.orders.order.validator;

import ekol.exceptions.ValidationException;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.orders.order.domain.OrderShipment;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class OrderShipmentDateValidator {

    public void validate(OrderShipment shipment){
        if(shipment.getReadyAtDate() == null &&
                (shipment.getLoadingAppointment() == null || shipment.getLoadingAppointment().getStartDateTime() == null)){
            throw new ValidationException("Order shipment should have a ready date or loading appointment");
        }

        if(shipment.getReadyAtDate() != null){
            if(shipment.getLoadingAppointment() != null && shipment.getLoadingAppointment().getStartDateTime() != null){
                throw new ValidationException("Order shipment should have either a ready date or a loading appointment");
            }
            if(shipment.getReadyAtDate().toZonedDateTime().isBefore(ZonedDateTime.now())){
                throw new ValidationException("Order shipment should have a future ready date");
            }
            if(shipment.getReadyAtDate().toZonedDateTime().isAfter(ZonedDateTime.now().plusYears(1))){
                throw new ValidationException("Order shipment should have a ready date within a year");
            }
        }
        if(shipment.getLoadingAppointment() != null && shipment.getLoadingAppointment().getStartDateTime() != null){
            if(shipment.getLoadingAppointment().getStartDateTime().toZonedDateTime().isBefore(ZonedDateTime.now())){
                throw new ValidationException("Order shipment should have a future loading date appointment");
            }
            if(shipment.getLoadingAppointment().getEndDateTime() != null){
                if(shipment.getLoadingAppointment().getStartDateTime().toZonedDateTime().isAfter(
                        shipment.getLoadingAppointment().getEndDateTime().toZonedDateTime())){
                    throw new ValidationException("Order shipment loading appointment should have an end date smaller than start date");
                }
            }
            if(shipment.getLoadingAppointment().getStartDateTime().toZonedDateTime().isAfter(ZonedDateTime.now().plusYears(1))){
                throw new ValidationException("Order shipment should have a loading appointment date within a year");
            }
        }

        if(shipment.getUnloadingAppointment() != null &&
                shipment.getUnloadingAppointment().getStartDateTime() != null && shipment.getUnloadingAppointment().getEndDateTime() != null){

            if(shipment.getUnloadingAppointment().getStartDateTime().toZonedDateTime().isAfter(
                    shipment.getUnloadingAppointment().getEndDateTime().toZonedDateTime())){
                throw new ValidationException("Order shipment unloading appointment should have an end date smaller than start date");
            }
        }
        FixedZoneDateTime earliestUnloading = shipment.getDeliveryDate() != null ? shipment.getDeliveryDate() : shipment.findReadyDate();
        if(earliestUnloading != null && shipment.getUnloadingAppointment() != null && shipment.getUnloadingAppointment().getStartDateTime() != null){
            ZonedDateTime beginningOfEarliestUnloading = earliestUnloading.toZonedDateTime().truncatedTo(ChronoUnit.DAYS);
            if(shipment.getUnloadingAppointment().getStartDateTime().toZonedDateTime().isBefore(beginningOfEarliestUnloading)){
                throw new ValidationException("Order shipment unloading appointment should be after delivery date and ready date");
            }
        }

        if(shipment.getDeliveryDate() != null &&
                shipment.findReadyDate().toZonedDateTime().isAfter(shipment.getDeliveryDate().toZonedDateTime())){
            throw new ValidationException("Order shipment ready date should be before delivery date");
        }

        if(shipment.getUnloadingAppointment() != null &&
                shipment.getUnloadingAppointment().getStartDateTime().toZonedDateTime().isAfter(shipment.findReadyDate().toZonedDateTime().plusDays(90L))){
            throw new ValidationException("Order shipment ready date should be 90 days prior to unloading appointment");
        }

    }
}
