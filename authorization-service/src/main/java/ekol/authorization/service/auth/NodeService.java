package ekol.authorization.service.auth;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.authorization.domain.auth.AuthSubsidiary;
import ekol.authorization.domain.auth.AuthTeam;
import ekol.authorization.domain.auth.BaseEntity;
import ekol.authorization.domain.auth.Label;
import ekol.authorization.domain.auth.LabelQueryResult;
import ekol.authorization.dto.Node;
import ekol.authorization.repository.auth.NodeRepository;
import ekol.exceptions.BadRequestException;
import ekol.model.User;
import ekol.resource.oauth2.SessionOwner;
import lombok.AllArgsConstructor;

/**
 * Created by ozer on 09/03/2017.
 */
@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class NodeService {

    private static final String MY_NODES_KEY = "MY_NODES";

    private SessionOwner sessionOwner;
    private Session session;
    private NodeRepository nodeRepository;
    private RedisTemplate<String, Object> redisTemplate;
    private Neo4jContextHelper neo4jContextHelper;
    
    public Iterable<BaseEntity> findByIdWithInheritRelation(Long id, int depth) {
    	Map<String, Object> params = new HashMap<>();
    	params.put("id", id);
    	return session.query(BaseEntity.class, "MATCH(n) WHERE ID(n)={id} WITH n MATCH p=(n)-[r:INHERIT*0.."+depth+"]-(m) return p", params);
    }

    public <T extends BaseEntity> T loadFromExternalId(Long externalId, Class<T> nodeClass){
        T baseEntity = null;
        Collection<T> collection = session.loadAll(nodeClass, new Filter("externalId", externalId), 1);
        if (collection != null && !collection.isEmpty()) {
            baseEntity = collection.iterator().next();
        }
        return baseEntity;
    }

    public <T extends BaseEntity> T loadFromName(String name, Class<T> nodeClass){
        T baseEntity = null;
        Collection<T> collection = session.loadAll(nodeClass, new Filter("name", name), 1);
        if (collection != null && !collection.isEmpty()) {
            baseEntity = collection.iterator().next();
        }
        return baseEntity;
    }

    @Transactional
    public <T extends BaseEntity> BaseEntity createOrLoadFromNode(Node node) {
        if (node == null) {
            throw new BadRequestException("Node can not be null");
        }

        if (!node.isValid()) {
            throw new BadRequestException("Node is not valid");
        }

        Class<T> nodeClass = neo4jContextHelper.findNodeEntityClassFromNodeType(node.getType());
        
        BaseEntity baseEntity = null;
        if (node.getId() != null) {
            baseEntity = session.load(nodeClass, node.getId());
            if (baseEntity == null) {
                throw new BadRequestException("Can not find entity with id");
            } else if(nodeClass.equals(AuthTeam.class) 
            		&& (Objects.isNull(baseEntity.getExternalId()) || Objects.equals(Long.valueOf(0), baseEntity.getExternalId()))) {
            		baseEntity.setExternalId(node.getExternalId());
            		session.save(baseEntity);
            }
        } else if (node.getExternalId() != null) {
        	baseEntity = loadFromExternalId(node.getExternalId(), nodeClass);
        }

        if (baseEntity == null) {
        	baseEntity = loadFromName(node.getName(), nodeClass);
        	if(Objects.nonNull(baseEntity) && Objects.nonNull(node.getExternalId())) {
        		baseEntity.setExternalId(node.getExternalId());
        		session.save(baseEntity);
        	}
        }
        
        if(Objects.nonNull(baseEntity)) {
        	return baseEntity;
        }

        // Bu kontrolü açık bir nokta bırakmadığımızdan emin olmak için koyduk.
        // Önceden relation oluşturulurken, varolmayan node'lar oluşturuluyordu. Ancak subsidiary'ler artık
        // manual olarak oluşturulmalı.
        if (nodeClass.equals(AuthSubsidiary.class)) {
            throw new BadRequestException("Subsidiaries must be created manually.");
        }

        BaseEntity newEntity;
        try {
            newEntity = nodeClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        newEntity.fillFromNode(node);

        if (newEntity.hasExternalId() && newEntity.getExternalId() == null) {
            throw new BadRequestException("Entity should have an external id");
        }
        session.save(newEntity);
        return newEntity;
    }

    public List<Object> findAllNodesExceptOperations() {
        return nodeRepository.findAllNodesExceptOperations();
    }

    public List<Object> findNodesByUserName(String username) {
        return nodeRepository.findNodesByUserName(username, LocalDate.now().toEpochDay());
    }

    public Collection<Object> myNodes() {

        User currentUser = sessionOwner.getCurrentUser();
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        List<Object> nodes = null;

        if (hashOperations.hasKey(MY_NODES_KEY, currentUser.getUsername())) {
            nodes = (List<Object>) hashOperations.get(MY_NODES_KEY, currentUser.getUsername());
        }

        if (CollectionUtils.isEmpty(nodes)) {
            nodes = nodeRepository.findNodesByUserName(currentUser.getUsername(), LocalDate.now().toEpochDay());
            hashOperations.put(MY_NODES_KEY, currentUser.getUsername(), nodes);
        }

        return nodes;
    }

    public void cleanCache() {
        redisTemplate.delete(MY_NODES_KEY);
    }

    public List<Label> findAllNodeLabelsForAuthorization() {
        List<Label> labels = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        Collection<LabelQueryResult> results = nodeRepository.findAllNodeLabelsForAuthorization();
        if (results != null && !results.isEmpty()) {
            results.forEach(result -> {
                Collection<String> names = result.getNames();
                if (names != null && !names.isEmpty()) {
                    names.forEach(name -> {
                        if (!seen.contains(name)) {
                            seen.add(name);

                            Label label = new Label();
                            label.setId(name);
                            label.setCode(name);
                            label.setName(name);
                            labels.add(label);
                        }
                    });
                }
            });
        }

        return labels;
    }
}
