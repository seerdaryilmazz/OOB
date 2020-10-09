package ekol.authorization.controller.auth;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.authorization.domain.auth.BaseEntity;
import ekol.authorization.domain.auth.Label;
import ekol.authorization.dto.Node;
import ekol.authorization.repository.auth.BaseRepository;
import ekol.authorization.service.auth.Neo4jContextHelper;
import ekol.authorization.service.auth.NodeService;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

/**
 * Created by ozer on 08/03/2017.
 */
@RestController
@RequestMapping("/node")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class NodeController {

    private NodeService nodeService;
    private BaseRepository baseRepository;
    private Neo4jContextHelper neo4jContextHelper;

    @GetMapping("/allExceptOperations")
    public List<Object> findAllNodesExceptOperations() {
        return nodeService.findAllNodesExceptOperations();
    }

    @GetMapping("/byUsername/{username}")
    public List<Object> findNodesByUserName(@PathVariable String username) {
        return nodeService.findNodesByUserName(username);
    }

    @GetMapping("/my")
    public Collection<Object> myNodes() {
        return nodeService.myNodes();
    }

    @GetMapping("/cache/clean")
    public void cleanCache() {
        nodeService.cleanCache();
    }

    @GetMapping("/label/allForAuthorization")
    public List<Label> findAllNodeLabelsExceptOperations() {
        return nodeService.findAllNodeLabelsForAuthorization();
    }
    
    @GetMapping("/{id}")
    public Node getNode(@PathVariable Long id) {
    	return Optional.ofNullable(baseRepository.findById(id)).map(Node::with).orElseThrow(()->new ResourceNotFoundException("node not found"));
    }
    
    @GetMapping("/{nodeType}/{externalId}")
    public <T extends BaseEntity> Node getNodeByExternalId(@PathVariable Long externalId, @PathVariable String nodeType) {
    	Class<T> nodeClass = neo4jContextHelper.findNodeEntityClassFromNodeType(StringUtils.capitalize(nodeType));
    	return Optional.of(nodeService.loadFromExternalId(externalId, nodeClass)).map(Node::with).orElseThrow(()->new ResourceNotFoundException("node not found"));
    }
}

