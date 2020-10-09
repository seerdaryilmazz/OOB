package ekol.authorization.repository;

import ekol.authorization.domain.Operation;
import ekol.hibernate5.domain.repository.ApplicationRepository;

/**
 * Created by ozer on 27/02/2017.
 */
public interface OperationRepository extends ApplicationRepository<Operation> {

    Operation findByName(String name);
}
