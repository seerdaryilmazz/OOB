package ekol.authorization.controller.auth;

import ekol.authorization.domain.auth.AuthSector;
import ekol.authorization.repository.auth.AuthSectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ozer on 08/03/2017.
 */
@RestController
@RequestMapping("/auth/sector")
public class AuthSectorController extends BaseController<AuthSector> {

    @Autowired
    private AuthSectorRepository authSectorRepository;

    @Override
    protected GraphRepository<AuthSector> getGraphRepository() {
        return authSectorRepository;
    }

    @Override
    protected Class getEntityClass() {
        return AuthSector.class;
    }
}
