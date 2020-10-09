package ekol.authorization.repository;

import ekol.authorization.domain.OperationUrl;
import ekol.hibernate5.domain.repository.ApplicationRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

/**
 * Created by ozer on 27/02/2017.
 */
public interface OperationUrlRepository extends ApplicationRepository<OperationUrl> {

    OperationUrl findByOperationIdAndUrl(Long operationId, String url);

    @EntityGraph(value = "OperationUrl.withOperation", type = EntityGraph.EntityGraphType.LOAD)
    List<OperationUrl> findByServiceName(String serviceName);

    OperationUrl findByOperationIsNullAndServiceNameAndUrl(String serviceName, String url);
}
