package ekol.location.domain.location.customerwarehouse;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.location.domain.location.comnon.workinghour.TimeWindow;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Set;

/**
 * Created by burak on 03/04/17.
 */
@Entity
@Table(name = "cw_bookingslot")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingSlot implements Serializable{


    public static final String TYPE_LOADING = "LOADING";
    public static final String TYPE_UNLOADING = "UNLOADING";

    @Id
    @SequenceGenerator(name = "seq_cw_bookingslot", sequenceName = "seq_cw_bookingslot")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cw_bookingslot")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_warehouse_id")
    @JsonBackReference
    private CustomerWarehouse customerWarehouse;

    private String type;

    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="cw_bookingslot_time",
            joinColumns=@JoinColumn(name = "cw_bookingslot_id"))
    @Embedded
    private Set<TimeWindow> timeWindows;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerWarehouse getCustomerWarehouse() {
        return customerWarehouse;
    }

    public void setCustomerWarehouse(CustomerWarehouse customerWarehouse) {
        this.customerWarehouse = customerWarehouse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Set<TimeWindow> getTimeWindows() {
        return timeWindows;
    }

    public void setTimeWindows(Set<TimeWindow> timeWindows) {
        this.timeWindows = timeWindows;
    }
}
