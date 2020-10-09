package ekol.authorization.domain.auth;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ekol.authorization.dto.Node;

/**
 * Created by ozer on 08/03/2017.
 */
public abstract class BaseEntity implements Serializable {

	@GraphId
    private Long id;

    private String name;

    private Long externalId;
    
    @JsonIgnore
    @Relationship(direction = Relationship.UNDIRECTED, type="INHERIT")
    private Set<Inherit> inherits = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }
    
    public Set<Inherit> getInherits() {
		return inherits;
	}

	public void setInherits(Set<Inherit> inherits) {
		this.inherits = inherits;
	}

	public void fillFromNode(Node node) {
        name = node.getName();
        externalId = node.getExternalId();
    }

    public boolean hasExternalId() {
        return true;
    }

    public String getType() {
        if (this.getClass().isAnnotationPresent(NodeEntity.class)) {
            NodeEntity nodeEntity = this.getClass().getAnnotation(NodeEntity.class);
            if (StringUtils.isNotBlank(nodeEntity.label())) {
                return nodeEntity.label();
            } else {
                return this.getClass().getSimpleName();
            }
        }

        return null;
    }
}
