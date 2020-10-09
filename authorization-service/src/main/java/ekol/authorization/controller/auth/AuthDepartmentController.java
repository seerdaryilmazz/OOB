package ekol.authorization.controller.auth;

import ekol.authorization.domain.auth.AuthDepartment;
import ekol.authorization.repository.auth.AuthDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ozer on 08/03/2017.
 */
@RestController
@RequestMapping("/auth/department")
public class AuthDepartmentController extends BaseController<AuthDepartment> {

    @Autowired
    private AuthDepartmentRepository authDepartmentRepository;

    @Override
    protected GraphRepository<AuthDepartment> getGraphRepository() {
        return authDepartmentRepository;
    }

    @Override
    protected Class getEntityClass() {
        return AuthDepartment.class;
    }
}
