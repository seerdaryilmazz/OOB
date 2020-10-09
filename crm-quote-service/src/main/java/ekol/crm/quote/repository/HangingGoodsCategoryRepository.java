package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.HangingGoodsCategory;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;

public interface HangingGoodsCategoryRepository extends ApplicationRepository<HangingGoodsCategory> {

    List<HangingGoodsCategory> findByOrderByName();

}
