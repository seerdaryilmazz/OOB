package ekol.authorization.service;

import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.authorization.domain.Operation;
import ekol.authorization.domain.auth.*;
import ekol.authorization.repository.OperationRepository;
import ekol.authorization.repository.auth.AuthOperationRepository;
import ekol.authorization.repository.auth.RelationRepository;
import ekol.authorization.service.auth.AuthOperationService;
import ekol.authorization.service.auth.NodeService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class InitService {
	
	private AuthOperationService authOperationService;
	private AuthOperationRepository authOperationRepository;
	private OperationRepository operationRepository;
    private RelationRepository relationRepository;
    private NodeService nodeService;
    
    private static final String DEFAULT_NODE_NAME = "IT";
    private static final String OPERATION_AUTH_NAME = "operation.manage";

    @PostConstruct
	public void init() {
		List<AuthOperation> nodes = authOperationRepository.findAuthorizedRelations(OPERATION_AUTH_NAME);
		
		if(nodes.stream().flatMap(n->n.getAuthorizations().stream()).map(AuthorizedRelation::getNode).map(BaseEntity::getName).noneMatch(DEFAULT_NODE_NAME::equals)){
			
			AuthOperation operationNode = null;
			AuthDepartment defaultNode = nodeService.loadFromName(DEFAULT_NODE_NAME, AuthDepartment.class);
			
			Operation operation = operationRepository.findByName(OPERATION_AUTH_NAME);
			
			if(Objects.nonNull(operation)) {
				operationNode = nodeService.loadFromExternalId(operation.getId(), AuthOperation.class);
				if(Objects.isNull(operationNode)) {
					operationNode = new AuthOperation();
					operationNode.setName(OPERATION_AUTH_NAME);
					operationNode.setExternalId(operation.getId());
					operationNode = authOperationRepository.save(operationNode);
				}
			}
			
			if(Objects.nonNull(defaultNode) && Objects.nonNull(operationNode)){
				relationRepository.mergeAuthorizedRelation(defaultNode.getId(), operationNode.getId(), 100L);
				authOperationService.cleanCache();
			}
		}
	}
}
