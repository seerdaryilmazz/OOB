package ekol.authorization.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.web.bind.annotation.*;

import ekol.authorization.domain.auth.AuthTeam;
import ekol.authorization.repository.auth.AuthTeamRepository;
import lombok.AllArgsConstructor;

/**
 * Created by ozer on 08/03/2017.
 */
@RestController
@RequestMapping("/auth/team")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class AuthTeamController extends BaseController<AuthTeam> {

    private AuthTeamRepository authTeamRepository;

    @Override
    protected GraphRepository<AuthTeam> getGraphRepository() {
        return authTeamRepository;
    }
    
    @Override
    protected Class<AuthTeam> getEntityClass() {
        return AuthTeam.class;
    }
}
