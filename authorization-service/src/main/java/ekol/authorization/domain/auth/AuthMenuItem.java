package ekol.authorization.domain.auth;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 18/04/2017.
 */
@NodeEntity(label = "MenuItem")
public class AuthMenuItem extends BaseEntity {

    @JsonManagedReference
    @Relationship(type = "CAN_VIEW", direction = Relationship.INCOMING)
    private List<CanViewRelation> viewers = new ArrayList<>();

    public List<CanViewRelation> getViewers() {
        return viewers;
    }

    public void setViewers(List<CanViewRelation> viewers) {
        this.viewers = viewers;
    }
}