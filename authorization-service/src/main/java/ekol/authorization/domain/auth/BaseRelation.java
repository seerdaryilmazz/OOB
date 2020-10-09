package ekol.authorization.domain.auth;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import lombok.Data;

@Data
public abstract class BaseRelation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2392712916454743242L;

	@GraphId
	private Long id;
	
	@StartNode
    private BaseEntity startNode;

    @EndNode
    private BaseEntity endNode;
	
	public String getType() {
        if (this.getClass().isAnnotationPresent(RelationshipEntity.class)) {
        	RelationshipEntity relationEntity = this.getClass().getAnnotation(RelationshipEntity.class);
            if (StringUtils.isNotBlank(relationEntity.type())) {
                return relationEntity.type();
            } else {
                return this.getClass().getSimpleName();
            }
        }

        return null;
    }
}
