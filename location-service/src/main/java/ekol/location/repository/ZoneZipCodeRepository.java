package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.ZoneZipCode;

import java.util.List;

/**
 * Created by ozer on 15/12/16.
 */
public interface ZoneZipCodeRepository extends ApplicationRepository<ZoneZipCode> {

    List<ZoneZipCode> findByZoneId(Long zoneId);
}
