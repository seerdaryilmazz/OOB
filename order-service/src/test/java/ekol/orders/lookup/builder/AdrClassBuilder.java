package ekol.orders.lookup.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.AdrClass;

public final class AdrClassBuilder {

    private Long id;
    private String code;
    private String name;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private AdrClassBuilder() {
    }

    public static AdrClassBuilder anAdrClass() {
        return new AdrClassBuilder();
    }

    public AdrClassBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public AdrClassBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public AdrClassBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AdrClassBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public AdrClassBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public AdrClassBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public AdrClassBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public AdrClass build() {
        AdrClass adrClass = new AdrClass();
        adrClass.setId(id);
        adrClass.setCode(code);
        adrClass.setName(name);
        adrClass.setDeleted(deleted);
        adrClass.setDeletedAt(this.deletedAt);
        adrClass.setLastUpdated(lastUpdated);
        adrClass.setLastUpdatedBy(lastUpdatedBy);
        return adrClass;
    }
}
