package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.location.customerwarehouse.BookingSlot;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by burak on 11/04/17.
 */
public interface CWBookingSlotRepository extends ApplicationRepository<BookingSlot> {

    @Transactional
    void removeByCustomerWarehouseId(Long customerWarehouseId);

    Set<BookingSlot> findByCustomerWarehouseIdAndType(Long customerWarehouseId, String type);

}