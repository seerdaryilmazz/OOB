package ekol.orders.order.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.ServiceType;
import ekol.orders.lookup.domain.TruckLoadType;
import ekol.orders.order.domain.*;

import java.util.ArrayList;
import java.util.List;

public final class OrderBuilder {
    private boolean deleted;
    private Long id;
    private String code;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private IdNameEmbeddable subsidiary;
    private UtcDateTime deletedAt;
    private IdNameEmbeddable customer;
    private IdNameEmbeddable originalCustomer;
    private boolean partnerCustomer;
    private ServiceType serviceType;
    private TruckLoadType truckLoadType;
    private Status status;
    private String countryCode;
    private String templateId;
    private String templateName;
    private List<OrderShipment> shipments = new ArrayList<>();
    private List<OrderDocument> documents = new ArrayList<>();

    private OrderBuilder() {
    }

    public static OrderBuilder anOrder() {
        return new OrderBuilder();
    }

    public OrderBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public OrderBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public OrderBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public OrderBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public OrderBuilder withSubsidiary(IdNameEmbeddable subsidiary) {
        this.subsidiary = subsidiary;
        return this;
    }

    public OrderBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public OrderBuilder withCustomer(IdNameEmbeddable customer) {
        this.customer = customer;
        return this;
    }

    public OrderBuilder withOriginalCustomer(IdNameEmbeddable originalCustomer) {
        this.originalCustomer = originalCustomer;
        return this;
    }

    public OrderBuilder withServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public OrderBuilder withTruckLoadType(TruckLoadType truckLoadType) {
        this.truckLoadType = truckLoadType;
        return this;
    }

    public OrderBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    public OrderBuilder withPartnerCustomer(boolean partnerCustomer) {
        this.partnerCustomer = partnerCustomer;
        return this;
    }

    public OrderBuilder withCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public OrderBuilder withTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }
    public OrderBuilder withTemplateName(String templateName) {
        this.templateName = templateName;
        return this;
    }

    public OrderBuilder withShipments(List<OrderShipment> shipments) {
        this.shipments = shipments;
        return this;
    }

    public OrderBuilder withDocuments(List<OrderDocument> documents) {
        this.documents = documents;
        return this;
    }

    public OrderBuilder but() {
        return anOrder().withDeleted(deleted).withId(id).withCode(code).withLastUpdated(lastUpdated).withLastUpdatedBy(lastUpdatedBy)
                .withSubsidiary(subsidiary).withDeletedAt(deletedAt).withCustomer(customer).withOriginalCustomer(originalCustomer)
                .withServiceType(serviceType).withTruckLoadType(truckLoadType).withStatus(status).withPartnerCustomer(partnerCustomer)
                .withTemplateId(templateId).withTemplateName(templateName).withCountryCode(countryCode)
                .withShipments(shipments).withDocuments(documents);
    }

    public Order build() {
        Order order = new Order();
        order.setDeleted(deleted);
        order.setId(id);
        order.setCode(code);
        order.setLastUpdated(lastUpdated);
        order.setLastUpdatedBy(lastUpdatedBy);
        order.setSubsidiary(subsidiary);
        order.setDeletedAt(deletedAt);
        order.setCustomer(customer);
        order.setOriginalCustomer(originalCustomer);
        order.setServiceType(serviceType);
        order.setTruckLoadType(truckLoadType);
        order.setPartnerCustomer(partnerCustomer);
        order.setStatus(status);
        order.setCountryCode(countryCode);
        order.setTemplateId(templateId);
        order.setTemplateName(templateName);
        order.setShipments(shipments);
        order.setDocuments(documents);
        return order;
    }
}
