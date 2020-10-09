package ekol.authorization.domain.auth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.neo4j.ogm.annotation.*;

import java.io.Serializable;


/**
 * Created by kilimci on 18/04/2017.
 */
@RelationshipEntity(type="CAN_VIEW")
public class CanViewRelation implements Serializable{

    @GraphId
    private Long id;

    @Property
    private Integer level;

    @StartNode
    private BaseEntity node;

    @EndNode
    @JsonBackReference
    private AuthMenuItem menuItem;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public BaseEntity getNode() {
        return node;
    }

    public void setNode(BaseEntity node) {
        this.node = node;
    }

    public AuthMenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(AuthMenuItem menuItem) {
        this.menuItem = menuItem;
    }
}
