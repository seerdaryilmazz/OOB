package ekol.kartoteks.repository;


import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.Sector;

import java.util.List;

/**
 * Created by kilimci on 15/03/16.
 */
public interface SectorRepository extends ApplicationRepository<Sector> {
    List<Sector> findByParent(Sector sector);
    List<Sector> findByParentIsNull();
    Sector findByCode(String code);
}
