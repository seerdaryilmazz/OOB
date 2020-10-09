package ekol.authorization.repository;

import ekol.authorization.domain.OperationUrlMethod;
import ekol.hibernate5.domain.repository.ApplicationRepository;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by ozer on 27/02/2017.
 */
public interface OperationUrlMethodRepository extends ApplicationRepository<OperationUrlMethod> {

    OperationUrlMethod findByOperationUrlIdAndMethod(Long operationUrlId, RequestMethod method);

    List<OperationUrlMethod> findByOperationUrlId(Long operationUrlId);
}
