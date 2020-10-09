package ekol.orders.lookup.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.PaymentMethod;

public final class PaymentMethodBuilder {

    private Long id;
    private String code;
    private String name;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private PaymentMethodBuilder() {
    }

    public static PaymentMethodBuilder aPaymentMethod() {
        return new PaymentMethodBuilder();
    }

    public PaymentMethodBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PaymentMethodBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public PaymentMethodBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PaymentMethodBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public PaymentMethodBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public PaymentMethodBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public PaymentMethodBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public PaymentMethod build() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(id);
        paymentMethod.setCode(code);
        paymentMethod.setName(name);
        paymentMethod.setDeleted(deleted);
        paymentMethod.setDeletedAt(deletedAt);
        paymentMethod.setLastUpdated(lastUpdated);
        paymentMethod.setLastUpdatedBy(lastUpdatedBy);
        return paymentMethod;
    }
}
