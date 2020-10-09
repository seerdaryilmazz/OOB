package ekol.authorization.controller.auth;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.authorization.domain.auth.AuthOperation;
import ekol.authorization.dto.AuthorizationRequest;
import ekol.authorization.dto.Node;
import ekol.authorization.repository.auth.AuthOperationRepository;
import ekol.authorization.service.auth.AuthOperationService;
import ekol.event.auth.Authorize;
import lombok.AllArgsConstructor;

/**
 * Created by ozer on 08/03/2017.
 */
@RestController
@RequestMapping("/auth/operation")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class AuthOperationController extends BaseController<AuthOperation> {

    private AuthOperationRepository authOperationRepository;
    private AuthOperationService authOperationService;

    @Override
    protected GraphRepository<AuthOperation> getGraphRepository() {
        return authOperationRepository;
    }

    @Override
    protected Class getEntityClass() {
        return AuthOperation.class;
    }

    @GetMapping(value = "/search")
    public AuthOperation findAuthorizationRelations(@RequestParam String name) {
        List<AuthOperation> operations = authOperationRepository.findAuthorizedRelations(name);
        return operations.isEmpty() ? null : operations.get(0);
    }

    @GetMapping(value = "/user/{username:.+}")
    public List<AuthOperation> findAuthorizedOperationsByUserName(@PathVariable String username) {
        return authOperationRepository.findAuthorizedOperationsByUserName(username, LocalDate.now().toEpochDay());
    }

    @Authorize(operations="operation.manage")
    @PostMapping(value = "/save")
    public void saveAuthorization(@RequestBody AuthorizationRequest authorizationRequest) {
        authOperationService.saveAuthorization(authorizationRequest);
    }

    @GetMapping(value = "/my")
    public Collection<AuthOperation> myOperations() {
        return authOperationService.myOperations();
    }

    @GetMapping(value = "/cache/clean")
    public void cleanCache() {
        authOperationService.cleanCache();
    }

    @GetMapping(value = "/inherited/{id}")
    public List<Node> inheritedAndAuthorizedOperations(@PathVariable Long id) {
    	return authOperationService.findInheritedAuthorizedOperations(id).stream().map(Node::with).collect(Collectors.toList());
    }

    @GetMapping(value = "/authorized/{id}")
    public List<Node> authorizedOperations(@PathVariable Long id) {
    	return authOperationService.findAuthorizedOperations(id).stream().map(Node::with).collect(Collectors.toList());
    }
}
