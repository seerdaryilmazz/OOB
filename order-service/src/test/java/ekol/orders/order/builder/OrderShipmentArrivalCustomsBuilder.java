package ekol.orders.order.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.order.domain.CodeNameEmbeddable;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentArrivalCustoms;

public final class OrderShipmentArrivalCustomsBuilder {
    private Long id;
    private boolean deleted;
    private OrderShipment shipment;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private IdNameEmbeddable customsAgent;
    private UtcDateTime deletedAt;
    private IdNameEmbeddable customsAgentLocation;
    private IdNameEmbeddable customsOffice;
    private IdNameEmbeddable customsLocation;
    private CodeNameEmbeddable customsType;

    private OrderShipmentArrivalCustomsBuilder() {
    }

    public static OrderShipmentArrivalCustomsBuilder anOrderShipmentArrivalCustoms() {
        return new OrderShipmentArrivalCustomsBuilder();
    }

    public OrderShipmentArrivalCustomsBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderShipmentArrivalCustomsBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public OrderShipmentArrivalCustomsBuilder withShipment(OrderShipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public OrderShipmentArrivalCustomsBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public OrderShipmentArrivalCustomsBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public OrderShipmentArrivalCustomsBuilder withCustomsAgent(IdNameEmbeddable customsAgent) {
        this.customsAgent = customsAgent;
        return this;
    }

    public OrderShipmentArrivalCustomsBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public OrderShipmentArrivalCustomsBuilder withCustomsAgentLocation(IdNameEmbeddable customsAgentLocation) {
        this.customsAgentLocation = customsAgentLocation;
        return this;
    }

    public OrderShipmentArrivalCustomsBuilder withCustomsOffice(IdNameEmbeddable customsOffice) {
        this.customsOffice = customsOffice;
        return this;
    }

    public OrderShipmentArrivalCustomsBuilder withCustomsLocation(IdNameEmbeddable customsLocation) {
        this.customsLocation = customsLocation;
        return this;
    }

    public OrderShipmentArrivalCustomsBuilder withCustomsType(CodeNameEmbeddable customsType) {
        this.customsType = customsType;
        return this;
    }

    public OrderShipmentArrivalCustomsBuilder but() {
        return anOrderShipmentArrivalCustoms().withId(id).withDeleted(deleted).withShipment(shipment).withLastUpdated(lastUpdated).withLastUpdatedBy(lastUpdatedBy).withCustomsAgent(customsAgent).withDeletedAt(deletedAt).withCustomsAgentLocation(customsAgentLocation).withCustomsOffice(customsOffice).withCustomsLocation(customsLocation).withCustomsType(customsType);
    }

    public OrderShipmentArrivalCustoms build() {
        OrderShipmentArrivalCustoms orderShipmentCustoms = new OrderShipmentArrivalCustoms();
        orderShipmentCustoms.setId(id);
        orderShipmentCustoms.setDeleted(deleted);
        orderShipmentCustoms.setShipment(shipment);
        orderShipmentCustoms.setLastUpdated(lastUpdated);
        orderShipmentCustoms.setLastUpdatedBy(lastUpdatedBy);
        orderShipmentCustoms.setCustomsAgent(customsAgent);
        orderShipmentCustoms.setDeletedAt(deletedAt);
        orderShipmentCustoms.setCustomsAgentLocation(customsAgentLocation);
        orderShipmentCustoms.setCustomsOffice(customsOffice);
        orderShipmentCustoms.setCustomsLocation(customsLocation);
        orderShipmentCustoms.setCustomsType(customsType);
        return orderShipmentCustoms;
    }
}
