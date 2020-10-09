package ekol.location.validator;

import ekol.exceptions.ValidationException;
import ekol.location.domain.location.customerwarehouse.CustomerWarehouse;
import ekol.location.domain.location.customerwarehouse.CustomerWarehouseBooking;
import ekol.location.domain.location.enumeration.CWBookingType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by burak on 11/04/17.
 */
@Component
public class CustomerWarehouseValidator {

    @Autowired
    private PlaceValidator placeValidator;

    public void validate(CustomerWarehouse customerWarehouse) {
        placeValidator.validate(customerWarehouse);
        this.validateGeneric(customerWarehouse);
        this.validateBooking(customerWarehouse);
        CustomsDetailsValidator.validate(customerWarehouse.getCustomsDetails(), customerWarehouse.getCountryIso());
    }

    private void validateGeneric(CustomerWarehouse customerWarehouse) {

        if (customerWarehouse.getCompany() == null) {
            throw new ValidationException("Company should be selected");
        } else if (!customerWarehouse.getCompany().isValid()) {
            throw new ValidationException("Company Data is invalid.");
        }

        if (customerWarehouse.getCompanyLocation() == null) {
            throw new ValidationException("Company Warehouse Location should be selected");
        } else if (!customerWarehouse.getCompanyLocation().isValid()) {
            throw new ValidationException("Company Location Data is invalid.");
        }
    }

    private void validateBooking(CustomerWarehouse customerWarehouse) {
        if(customerWarehouse.getBookingLoading() == null){
            throw new ValidationException("Loading booking information should not be empty.");
        }
        if(customerWarehouse.getBookingLoading().isAlwaysBooking()){
            this.validateAlwaysBooking(customerWarehouse.getBookingLoading());
        }

        if(customerWarehouse.getBookingUnloading() == null){
            throw new ValidationException("Unloading booking information should not be empty.");
        }
        if(customerWarehouse.getBookingUnloading().isAlwaysBooking()){
            this.validateAlwaysBooking(customerWarehouse.getBookingUnloading());
        }

    }

    private void validateAlwaysBooking(CustomerWarehouseBooking booking) {
        this.validateBookingType(booking);
        this.validateBookingAtStatus(booking);
    }

    private void validateBookingType(CustomerWarehouseBooking booking) {
        if (StringUtils.isEmpty(booking.getBookingUrl()) && booking.getBookingContact() == null) {
            throw new ValidationException("Booking Url or Booking Contact person should be entered.");
        }
        if (StringUtils.isNotEmpty(booking.getBookingUrl()) && booking.getBookingContact() != null) {
            throw new ValidationException("Only one Method should be selected: Booking Url or Contact Person.");
        }
        if (booking.getBookingContact() != null && !booking.getBookingContact().isValid()) {
            throw new ValidationException("Booking Contact Data is invalid.");
        }
    }
    private void validateBookingAtStatus(CustomerWarehouseBooking booking) {
        if (booking.getBookingType() == null) {
            throw new ValidationException("Booking At Status should be selected.");
        } else if (booking.getBookingType() == CWBookingType.ORDER_REQUEST) {
            if (booking.getBookingDaysBefore() != null || StringUtils.isNotBlank(booking.getBookingTimeUntil())) {
                throw new ValidationException("Booking At Status Time should be empty.");
            }
        } else {
            if (booking.getBookingDaysBefore() == null || StringUtils.isBlank(booking.getBookingTimeUntil())) {
                throw new ValidationException("Booking At Status time should not be empty.");
            } else if (booking.getBookingDaysBefore() < 0) {
                throw new ValidationException("Days before handling time can not be negative");
            }
        }
    }

}
