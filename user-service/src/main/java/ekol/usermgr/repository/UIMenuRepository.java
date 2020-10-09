package ekol.usermgr.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.usermgr.domain.UIMenu;

import java.util.List;

/**
 * Created by burak on 15/08/16.
 */
public interface UIMenuRepository extends ApplicationRepository<UIMenu> {

	Iterable<UIMenu> findAllByIdNotIn(Iterable<Long> id);
	Iterable<UIMenu> findAllByIdIn(Iterable<Long> id);
    Iterable<UIMenu> findAllByOrderByIdAsc();
    Iterable<UIMenu> findAllByOrderByRankAsc();
    List<UIMenu> findAllByParentOrderByRankAsc(UIMenu parent);

}
