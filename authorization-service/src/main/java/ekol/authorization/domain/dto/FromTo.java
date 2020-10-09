package ekol.authorization.domain.dto;

import ekol.authorization.domain.auth.BaseEntity;

public class FromTo {
    private final BaseEntity from;
    private final BaseEntity to;

    public FromTo(BaseEntity from, BaseEntity to) {
        this.from = from;
        this.to = to;
    }

    public BaseEntity getFrom() {
        return from;
    }

    public BaseEntity getTo() {
        return to;
    }
}
