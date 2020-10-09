package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.WarehouseCompanyType;
import ekol.location.domain.WarehouseCustomsType;
import ekol.location.domain.location.customerwarehouse.CustomerWarehouse;
import ekol.location.domain.location.customs.CustomsOffice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by ozer on 01/11/16.
 */
public interface CustomerWarehouseRepository extends ApplicationRepository<CustomerWarehouse>, JpaSpecificationExecutor<CustomerWarehouse> {

    CustomerWarehouse findById(Long id);

    List<CustomerWarehouse> findByCompanyLocationIdAndCustomsDetailsNotNull(Long companyLocationId);

    CustomerWarehouse findByIdAndCustomsDetailsNotNull(Long id);

    CustomerWarehouse findByCompanyLocationId(Long companyLocationId);

    List<CustomerWarehouse> findByCustomsDetailsEuropeanCustomsCodeNotNull();
    
    List<CustomerWarehouse> findByCustomsDetailsCustomsType(WarehouseCustomsType customsType);

    List<CustomerWarehouse> findByCompanyId(Long companyId);

    CustomerWarehouse findByCompanyLocationIdAndCompanyType(Long companyLocationId, WarehouseCompanyType type);

    List<CustomerWarehouse> findByCustomsDetailsCustomsOfficeAndCustomsDetailsCustomsType(
            CustomsOffice customsOffice, WarehouseCustomsType type);
    
    long countByCompanyLocationIdIn(List<Long> companyLocationIds);
}