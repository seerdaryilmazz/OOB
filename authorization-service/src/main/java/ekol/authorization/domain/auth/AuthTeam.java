package ekol.authorization.domain.auth;

import org.neo4j.ogm.annotation.NodeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by ozer on 08/03/2017.
 */
@Data
@EqualsAndHashCode(callSuper=true)
@NodeEntity(label = "Team")
public class AuthTeam extends BaseEntity {

}
