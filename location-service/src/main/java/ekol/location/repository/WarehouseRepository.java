package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.WarehouseCustomsType;
import ekol.location.domain.location.customs.CustomsOffice;
import ekol.location.domain.location.warehouse.Warehouse;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by burak on 09/02/17.
 */
@Repository
public interface WarehouseRepository extends ApplicationRepository<Warehouse>, JpaSpecificationExecutor<Warehouse> {

    Warehouse findByCompanyLocationId(Long companyLocationId);

    List<Warehouse> findByCompanyId(Long companyId);

    List<Warehouse> findByCustomsDetailsEuropeanCustomsCodeNotNull();

    List<Warehouse> findByCustomsDetailsCustomsType(WarehouseCustomsType customsType);

    Optional<Warehouse> findByIdAndCustomsDetailsNotNull(Long companyId);

    Optional<Warehouse> findByCompanyLocationIdAndCustomsDetailsNotNull(Long companyLocationId);

    List<Warehouse> findByRouteLegStopId(Long routeLegStopId);

    List<Warehouse> findByCustomsDetailsCustomsOfficeAndCustomsDetailsCustomsType(
            CustomsOffice customsOffice, WarehouseCustomsType type);
    
    long countByCompanyLocationIdIn(List<Long> companyLocationIds);
}
