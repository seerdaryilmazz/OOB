package ekol.orders.transportOrder.repository;

import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.orders.transportOrder.domain.EquipmentType;

import java.util.Optional;

public interface EquipmentTypeRepository extends LookupRepository<EquipmentType> {

    Optional<EquipmentType> findById(long id);
}
