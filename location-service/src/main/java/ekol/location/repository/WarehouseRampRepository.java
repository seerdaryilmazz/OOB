package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.location.warehouse.WarehouseRamp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by burak on 09/02/17.
 */
@Repository
public interface WarehouseRampRepository extends ApplicationRepository<WarehouseRamp> {

    public List<WarehouseRamp> findAllByWarehouseId(Long warehouseId);

    public WarehouseRamp findOneByWarehouseIdAndId(Long warehouseId, Long warehouseRampId);


}
