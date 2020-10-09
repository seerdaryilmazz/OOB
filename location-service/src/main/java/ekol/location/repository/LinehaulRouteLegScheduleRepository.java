package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.LinehaulRouteLegSchedule;

public interface LinehaulRouteLegScheduleRepository extends ApplicationRepository<LinehaulRouteLegSchedule> {

    Iterable<LinehaulRouteLegSchedule> findAllByParentId(Long parentId);

}
