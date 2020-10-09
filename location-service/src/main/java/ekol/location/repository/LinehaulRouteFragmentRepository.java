package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.LinehaulRouteFragment;

public interface LinehaulRouteFragmentRepository extends ApplicationRepository<LinehaulRouteFragment> {

    Iterable<LinehaulRouteFragment> findAllByParentId(Long parentId);

    Iterable<LinehaulRouteFragment> findAllByOrderNoAndLegId(Integer orderNo, Long legId);

    Iterable<LinehaulRouteFragment> findAllByOrderNoAndRouteId(Integer orderNo, Long routeId);

    LinehaulRouteFragment findByParentIdAndLegId(Long parentId, Long legId);

    LinehaulRouteFragment findByParentIdAndRouteId(Long parentId, Long routeId);

    LinehaulRouteFragment findByParentIdAndOrderNo(Long parentId, Integer orderNo);

}
