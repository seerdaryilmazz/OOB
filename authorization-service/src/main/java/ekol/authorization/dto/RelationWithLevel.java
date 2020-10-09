package ekol.authorization.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by ozer on 08/03/2017.
 */
public abstract class RelationWithLevel extends Relation {

    private Long level;

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    @JsonIgnore
    public boolean isValid() {
        return super.isValid() && level != null;
    }
}
