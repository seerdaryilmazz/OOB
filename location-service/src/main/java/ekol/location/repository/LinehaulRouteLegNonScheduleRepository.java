package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.LinehaulRouteLegNonSchedule;

public interface LinehaulRouteLegNonScheduleRepository extends ApplicationRepository<LinehaulRouteLegNonSchedule> {

    Iterable<LinehaulRouteLegNonSchedule> findAllByParentId(Long parentId);

}
