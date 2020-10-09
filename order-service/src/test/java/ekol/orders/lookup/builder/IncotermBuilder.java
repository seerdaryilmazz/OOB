package ekol.orders.lookup.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.Incoterm;

public final class IncotermBuilder {

    private Long id;
    private String code;
    private String name;
    private String description;
    private boolean active;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private IncotermBuilder() {
    }

    public static IncotermBuilder anIncoterm() {
        return new IncotermBuilder();
    }

    public IncotermBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public IncotermBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public IncotermBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public IncotermBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public IncotermBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public IncotermBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public IncotermBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    public IncotermBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public IncotermBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public Incoterm build() {
        Incoterm incoterm = new Incoterm();
        incoterm.setId(id);
        incoterm.setCode(code);
        incoterm.setName(name);
        incoterm.setDescription(description);
        incoterm.setDeleted(deleted);
        incoterm.setDeletedAt(deletedAt);
        incoterm.setActive(active);
        incoterm.setLastUpdated(lastUpdated);
        incoterm.setLastUpdatedBy(lastUpdatedBy);
        return incoterm;
    }
}
