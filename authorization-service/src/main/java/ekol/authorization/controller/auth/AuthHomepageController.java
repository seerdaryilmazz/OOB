package ekol.authorization.controller.auth;

import ekol.authorization.domain.auth.AuthHomepage;
import ekol.authorization.repository.auth.AuthHomepageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/homepage")
public class AuthHomepageController extends BaseController<AuthHomepage>{

    @Autowired
    private AuthHomepageRepository repository;

    @Override
    protected GraphRepository<AuthHomepage> getGraphRepository() {
        return repository;
    }

    @Override
    protected Class getEntityClass() {
        return AuthHomepage.class;
    }
}
