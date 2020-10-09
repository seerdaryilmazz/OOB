package ekol.authorization.repository.auth;

import ekol.authorization.domain.auth.AuthSector;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ozer on 08/03/2017.
 */
@Repository
public interface AuthSectorRepository extends GraphRepository<AuthSector> {
}
