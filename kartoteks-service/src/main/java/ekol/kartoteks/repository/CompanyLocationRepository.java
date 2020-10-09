package ekol.kartoteks.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.domain.LocationType;
import ekol.kartoteks.domain.RemoteApplication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by fatmaozyildirim on 3/16/16.
 */

public interface CompanyLocationRepository extends ApplicationRepository<CompanyLocation> {

    Set<CompanyLocation> findByCompanyId(Long companyId);

    CompanyLocation findByCompanyIdAndIsDefault(Long companyId, boolean isDefault);

    List<CompanyLocation> findByCompanyIdInAndIsDefault(List<Long> companyIds, boolean isDefault);

    List<CompanyLocation> findByCompanyIdAndLocationTypes(Long companyId, LocationType locationType);

    @EntityGraph(value = "CompanyLocation.company", type = EntityGraph.EntityGraphType.LOAD)
    List<CompanyLocation> findByCompanyOwnedByEkolIsTrue();

    List<CompanyLocation> findByNameAndIdNot(String name, Long id);

    List<CompanyLocation> findByShortNameAndIdNot(String name, Long id);

    List<CompanyLocation> findByName(String name);

    List<CompanyLocation> findByShortName(String name);

    CompanyLocation findByCompanyIdAndName(Long companyId, String name);

    CompanyLocation findByCompanyIdAndShortName(Long companyId, String name);

    @EntityGraph(value = "CompanyLocation.company", type = EntityGraph.EntityGraphType.LOAD)
    List<CompanyLocation> findByShortNameIsNull();
    
    List<CompanyLocation> findByTimezoneIsNull();

    long countByIdAndActiveTrue(Long id);
    
    @Query("select cl.id from CompanyLocation cl where cl.active = true and cl.id in (:ids)")
    List<Long> findIdsByActiveTrueAndIdIn(@Param("ids") List<Long> ids);

    CompanyLocation findByMappedIds_applicationIsAndMappedIds_applicationLocationIdIs(RemoteApplication application, String appLocationId);
    
    Set<CompanyLocation> findByCompanyIdAndActiveTrue(Long companyId);
    
}
