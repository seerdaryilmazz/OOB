package ekol.authorization.domain.auth;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ozer on 08/03/2017.
 */
@NodeEntity(label = "User")
public class AuthUser extends BaseEntity {

    @JsonManagedReference
    @Relationship(type = "MEMBER_OF", direction = Relationship.OUTGOING)
    private Set<MemberOfRelation> memberships = new HashSet<>();

    public Set<MemberOfRelation> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<MemberOfRelation> memberships) {
        this.memberships = memberships;
    }
}
