package ekol.authorization.dto;

import java.util.*;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.fasterxml.jackson.annotation.*;

import ekol.authorization.domain.auth.BaseEntity;
import lombok.Data;

/**
 * Created by ozer on 08/03/2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Node implements Comparable<Node> {

	private Long id;
    private Long externalId;
    private String type;
    private String name;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<Node> parents;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<Node> children;
    
    public int hashCode() {
    	return new HashCodeBuilder().append(id).toHashCode();
    }
    
    public boolean equals(Object object) {

    	if (!(object instanceof Node))
            return false;
        if (object == this)
            return true;

        Node node = (Node) object;
        return new EqualsBuilder().
                append(getId(), node.getId()).
                isEquals();
    }
    
    public static Node with(BaseEntity entity) {
    	Node node = new Node();
    	node.setExternalId(entity.getExternalId());
    	node.setId(entity.getId());
    	node.setName(entity.getName());
    	node.setType(entity.getType());
    	return node;
    }
    
    @JsonIgnore
    public boolean isValid() {
        return StringUtils.isNotBlank(name)
                && StringUtils.isNotBlank(type);
    }

	@Override
	public int compareTo(Node o) {
		return Comparator.comparing(Node::getName).compare(this, o);
	}
}
