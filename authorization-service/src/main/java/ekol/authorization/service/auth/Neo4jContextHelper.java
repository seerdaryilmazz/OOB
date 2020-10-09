package ekol.authorization.service.auth;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.mapping.Neo4jMappingContext;
import org.springframework.data.util.TypeInformation;
import org.springframework.stereotype.Component;

import ekol.authorization.domain.auth.BaseEntity;
import ekol.exceptions.BadRequestException;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class Neo4jContextHelper {
    private Neo4jMappingContext neo4jMappingContext;
	
	private final Map<String, Class> nodeEntityClasses = new HashMap<>();

    @PostConstruct
    private void init() {
        Iterator<TypeInformation<?>> iter = neo4jMappingContext.getManagedTypes().iterator();
        while (iter.hasNext()) {
            Class typeClass = iter.next().getType();
            if (typeClass.isAnnotationPresent(NodeEntity.class)) {
                NodeEntity nodeEntity = (NodeEntity) typeClass.getAnnotation(NodeEntity.class);
                if (StringUtils.isNotBlank(nodeEntity.label())) {
                    nodeEntityClasses.put(nodeEntity.label(), typeClass);
                } else {
                    nodeEntityClasses.put(typeClass.getSimpleName(), typeClass);
                }
            }
        }
    }

    public <T extends BaseEntity> Class<T> findNodeEntityClassFromNodeType(String type) {
        if (StringUtils.isBlank(type)) {
            throw new RuntimeException("Type can not be empty");
        }

        Class<T> clazz = (Class<T>) nodeEntityClasses.get(type);
        if (null == clazz) {
            throw new BadRequestException("Invalid node type");
        }
        return clazz;
    }
}
