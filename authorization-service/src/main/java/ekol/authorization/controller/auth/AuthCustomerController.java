package ekol.authorization.controller.auth;

import ekol.authorization.domain.auth.AuthCustomer;
import ekol.authorization.repository.auth.AuthCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ozer on 08/03/2017.
 */
@RestController
@RequestMapping("/auth/customer")
public class AuthCustomerController extends BaseController<AuthCustomer> {

    @Autowired
    private AuthCustomerRepository authCustomerRepository;

    @Override
    protected GraphRepository<AuthCustomer> getGraphRepository() {
        return authCustomerRepository;
    }

    @Override
    protected Class getEntityClass() {
        return AuthCustomer.class;
    }
}
