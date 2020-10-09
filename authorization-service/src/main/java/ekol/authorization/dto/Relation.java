package ekol.authorization.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by ozer on 08/03/2017.
 */
public abstract class Relation {

    private Node from;
    private Node to;

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    @JsonIgnore
    public boolean isValid() {
        return from != null && from.isValid()
                && to != null && to.isValid();
    }
}
