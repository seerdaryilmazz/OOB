package ekol.agreement.repository;

import ekol.agreement.domain.model.HistoryUnitPrice;
import ekol.hibernate5.domain.repository.ApplicationRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface HistoryUnitPriceRepository extends ApplicationRepository<HistoryUnitPrice> {

    List<HistoryUnitPrice> findByUnitPriceId(Long unitPriceId, Sort sort);
}
