package ekol.agreement.repository;

import ekol.agreement.domain.model.HistoryModel;
import ekol.hibernate5.domain.repository.ApplicationRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface HistoryModelRepository extends ApplicationRepository<HistoryModel> {

    List<HistoryModel> findByModelId(Long modelId, Sort sort);
}
