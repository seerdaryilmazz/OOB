package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.location.warehouse.WarehouseZone;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by burak on 09/02/17.
 */
@Repository
public interface WarehouseZoneRepository extends ApplicationRepository<WarehouseZone> {

    public List<WarehouseZone> findAllByWarehouseId(Long warehouseId);

    public WarehouseZone findOneByWarehouseIdAndId(Long warehouseId, Long warehouseZoneId);


}