package ekol.authorization.service.auth;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.authorization.domain.auth.*;
import ekol.authorization.dto.AuthorizationRequest;
import ekol.authorization.repository.auth.*;
import ekol.model.User;
import ekol.resource.oauth2.SessionOwner;

/**
 * Created by ozer on 10/03/2017.
 */
@Service
public class AuthOperationService {

    private static final String MY_OPERATIONS_KEY = "MY_OPERATIONS";

    @Autowired
    private SessionOwner sessionOwner;

    @Autowired
    private AuthOperationRepository authOperationRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public boolean hasOpeartion(String operation) {
    	return myOperations().stream().map(AuthOperation::getName).anyMatch(operation::equalsIgnoreCase);
    }

    public Collection<AuthOperation> myOperations() {
        User currentUser = sessionOwner.getCurrentUser();
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        if (hashOperations.hasKey(MY_OPERATIONS_KEY, currentUser.getUsername())) {
            return (Collection<AuthOperation>) hashOperations.get(MY_OPERATIONS_KEY, currentUser.getUsername());
        } else {
            List<AuthOperation> authOperations = authOperationRepository.findAuthorizedOperationsByUserName(currentUser.getUsername(), LocalDate.now().toEpochDay());
            hashOperations.put(MY_OPERATIONS_KEY, currentUser.getUsername(), authOperations);
            return authOperations;
        }
    }

    @Transactional
    public void saveAuthorization(AuthorizationRequest authorizationRequest) {
        if(authorizationRequest.getOperation() != null){
            authOperationRepository.deleteOperationAuthRelation(authorizationRequest.getOperation().getName());
            AuthOperation operation = nodeService.loadFromName(authorizationRequest.getOperation().getName(), AuthOperation.class);
            if(operation != null){
                operation.getAuthorizations().clear();
            }
        }
        authorizationRequest.getAuthorizations().forEach(authorization -> {
            BaseEntity from = nodeService.createOrLoadFromNode(authorization.getFrom());
            BaseEntity to = nodeService.createOrLoadFromNode(authorization.getTo());
            relationRepository.mergeAuthorizedRelation(from.getId(), to.getId(), authorization.getLevel());
        });

        cleanCache();
    }



    public void cleanCache() {
        redisTemplate.delete(MY_OPERATIONS_KEY);
    }
    
    public List<AuthOperation> findInheritedAuthorizedOperations(Long id){
    	return authOperationRepository.findInheritedAutorizedOperations(id);
    }

    public List<AuthOperation> findAuthorizedOperations(Long id){
    	return authOperationRepository.findAutorizedOperations(id);
    }
}
