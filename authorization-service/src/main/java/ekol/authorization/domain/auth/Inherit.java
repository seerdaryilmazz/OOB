package ekol.authorization.domain.auth;

import org.neo4j.ogm.annotation.RelationshipEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@RelationshipEntity(type="INHERIT")
@EqualsAndHashCode(callSuper=true)
public class Inherit extends BaseRelation{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6992745313945632313L;

	
}
