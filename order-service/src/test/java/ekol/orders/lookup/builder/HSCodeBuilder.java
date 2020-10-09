package ekol.orders.lookup.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.HSCode;

public final class HSCodeBuilder {

    private Long id;
    private Short tier;
    private String code;
    private String name;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private HSCodeBuilder() {
    }

    public static HSCodeBuilder aHSCode() {
        return new HSCodeBuilder();
    }

    public HSCodeBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public HSCodeBuilder withTier(Short tier) {
        this.tier = tier;
        return this;
    }

    public HSCodeBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public HSCodeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public HSCodeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public HSCodeBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public HSCodeBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public HSCodeBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public HSCode build() {
        HSCode hSCode = new HSCode();
        hSCode.setId(id);
        hSCode.setTier(tier);
        hSCode.setCode(code);
        hSCode.setName(name);
        hSCode.setDeleted(deleted);
        hSCode.setDeletedAt(deletedAt);
        hSCode.setLastUpdated(lastUpdated);
        hSCode.setLastUpdatedBy(lastUpdatedBy);
        return hSCode;
    }
}
