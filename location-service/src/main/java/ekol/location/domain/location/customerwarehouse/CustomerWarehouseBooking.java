package ekol.location.domain.location.customerwarehouse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.location.domain.location.comnon.IdNameEmbeddable;
import ekol.location.domain.location.enumeration.CWBookingOptions;
import ekol.location.domain.location.enumeration.CWBookingType;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Created by burak on 03/05/17.
 */
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerWarehouseBooking {

    private String bookingUrl;

    @Embedded
    private IdNameEmbeddable bookingContact;

    private CWBookingOptions bookingOption;

    private CWBookingType bookingType;

    private Integer bookingDaysBefore;

    private String bookingTimeUntil;

    public boolean isAlwaysBooking(){
        return CWBookingOptions.ALWAYS.equals(getBookingOption());
    }
    public boolean isNeverBooking(){
        return CWBookingOptions.NEVER.equals(getBookingOption());
    }
    public boolean isUnknownBooking(){
        return CWBookingOptions.ASK.equals(getBookingOption()) || getBookingOption() == null;
    }

    public String getBookingUrl() {
        return bookingUrl;
    }

    public void setBookingUrl(String bookingUrl) {
        this.bookingUrl = bookingUrl;
    }

    public IdNameEmbeddable getBookingContact() {
        return bookingContact;
    }

    public void setBookingContact(IdNameEmbeddable bookingContact) {
        this.bookingContact = bookingContact;
    }

    public CWBookingType getBookingType() {
        return bookingType;
    }

    public void setBookingType(CWBookingType bookingType) {
        this.bookingType = bookingType;
    }

    public Integer getBookingDaysBefore() {
        return bookingDaysBefore;
    }

    public void setBookingDaysBefore(Integer bookingDaysBefore) {
        this.bookingDaysBefore = bookingDaysBefore;
    }

    public String getBookingTimeUntil() {
        return bookingTimeUntil;
    }

    public void setBookingTimeUntil(String bookingTimeUntil) {
        this.bookingTimeUntil = bookingTimeUntil;
    }

    public CWBookingOptions getBookingOption() {
        return bookingOption;
    }

    public void setBookingOption(CWBookingOptions bookingOption) {
        this.bookingOption = bookingOption;
    }
}
