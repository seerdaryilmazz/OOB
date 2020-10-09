package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.ZoneTag;

import java.util.List;

/**
 * Created by ozer on 15/12/16.
 */
public interface ZoneTagRepository extends ApplicationRepository<ZoneTag> {

    List<ZoneTag> findByZoneId(Long zoneId);
}
