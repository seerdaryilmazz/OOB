package ekol.authorization.domain.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by ozer on 08/03/2017.
 */
@NodeEntity(label = "Subsidiary")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthSubsidiary extends BaseEntity {

}
