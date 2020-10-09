package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.transportOrder.domain.TransportType;


public final class TransportTypeBuilder {

    private Long id;
    private String code;
    private String name;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private TransportTypeBuilder() {
    }

    public static TransportTypeBuilder aTransportType() {
        return new TransportTypeBuilder();
    }

    public TransportTypeBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TransportTypeBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public TransportTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TransportTypeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public TransportTypeBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public TransportTypeBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public TransportTypeBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public TransportType build() {
        TransportType transportType = new TransportType();
        transportType.setId(id);
        transportType.setCode(code);
        transportType.setName(name);
        transportType.setDeleted(deleted);
        transportType.setDeletedAt(deletedAt);
        transportType.setLastUpdated(lastUpdated);
        transportType.setLastUpdatedBy(lastUpdatedBy);
        return transportType;
    }
}
