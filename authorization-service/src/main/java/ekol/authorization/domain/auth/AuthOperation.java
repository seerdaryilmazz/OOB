package ekol.authorization.domain.auth;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Created by ozer on 08/03/2017.
 */
@NodeEntity(label = "Operation")
public class AuthOperation extends BaseEntity {

	@JsonManagedReference
    @Relationship(type = "AUTHORIZED", direction = Relationship.INCOMING)
    private List<AuthorizedRelation> authorizations = new ArrayList<>();

    public List<AuthorizedRelation> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<AuthorizedRelation> authorizations) {
        this.authorizations = authorizations;
    }
}
