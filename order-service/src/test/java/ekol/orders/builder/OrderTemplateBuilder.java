package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.domain.PaymentMethod;
import ekol.orders.lookup.domain.ServiceType;
import ekol.orders.lookup.domain.TruckLoadType;
import ekol.orders.transportOrder.domain.CurrencyType;
import ekol.orders.transportOrder.domain.OrderTemplate;

import java.util.HashSet;
import java.util.Set;

public final class OrderTemplateBuilder {

    private Long id;
    private String code;
    private String name;
    private Set<ServiceType> serviceTypes = new HashSet<>();
    private Set<Incoterm> incoterms = new HashSet<>();
    private Set<TruckLoadType> truckLoadTypes = new HashSet<>();
    private Set<PaymentMethod> paymentMethods = new HashSet<>();
    private Set<CurrencyType> currencyTypes = new HashSet<>();
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private OrderTemplateBuilder() {
    }

    public static OrderTemplateBuilder anOrderTemplate() {
        return new OrderTemplateBuilder();
    }

    public OrderTemplateBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderTemplateBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public OrderTemplateBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public OrderTemplateBuilder withServiceTypes(Set<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
        return this;
    }

    public OrderTemplateBuilder withIncoterms(Set<Incoterm> incoterms) {
        this.incoterms = incoterms;
        return this;
    }

    public OrderTemplateBuilder withTruckLoadTypes(Set<TruckLoadType> truckLoadTypes) {
        this.truckLoadTypes = truckLoadTypes;
        return this;
    }

    public OrderTemplateBuilder withPaymentMethods(Set<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
        return this;
    }

    public OrderTemplateBuilder withCurrencyTypes(Set<CurrencyType> currencyTypes) {
        this.currencyTypes = currencyTypes;
        return this;
    }

    public OrderTemplateBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public OrderTemplateBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public OrderTemplateBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public OrderTemplateBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public OrderTemplate build() {
        OrderTemplate orderTemplate = new OrderTemplate();
        orderTemplate.setId(id);
        orderTemplate.setCode(code);
        orderTemplate.setName(name);
        orderTemplate.setServiceTypes(serviceTypes);
        orderTemplate.setIncoterms(incoterms);
        orderTemplate.setTruckLoadTypes(truckLoadTypes);
        orderTemplate.setPaymentMethods(paymentMethods);
        orderTemplate.setCurrencyTypes(currencyTypes);
        orderTemplate.setDeleted(deleted);
        orderTemplate.setDeletedAt(deletedAt);
        orderTemplate.setLastUpdated(lastUpdated);
        orderTemplate.setLastUpdatedBy(lastUpdatedBy);
        return orderTemplate;
    }
}
