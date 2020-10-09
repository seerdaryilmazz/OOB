package ekol.orders.order.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentDepartureCustoms;

public final class OrderShipmentDepartureCustomsBuilder {
    private boolean deleted;
    private Long id;
    private OrderShipment shipment;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private IdNameEmbeddable customsAgent;
    private UtcDateTime deletedAt;
    private IdNameEmbeddable customsAgentLocation;
    private IdNameEmbeddable customsOffice;

    private OrderShipmentDepartureCustomsBuilder() {
    }

    public static OrderShipmentDepartureCustomsBuilder anOrderShipmentDepartureCustoms() {
        return new OrderShipmentDepartureCustomsBuilder();
    }

    public OrderShipmentDepartureCustomsBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public OrderShipmentDepartureCustomsBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderShipmentDepartureCustomsBuilder withShipment(OrderShipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public OrderShipmentDepartureCustomsBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public OrderShipmentDepartureCustomsBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public OrderShipmentDepartureCustomsBuilder withCustomsAgent(IdNameEmbeddable customsAgent) {
        this.customsAgent = customsAgent;
        return this;
    }

    public OrderShipmentDepartureCustomsBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public OrderShipmentDepartureCustomsBuilder withCustomsAgentLocation(IdNameEmbeddable customsAgentLocation) {
        this.customsAgentLocation = customsAgentLocation;
        return this;
    }

    public OrderShipmentDepartureCustomsBuilder withCustomsOffice(IdNameEmbeddable customsOffice) {
        this.customsOffice = customsOffice;
        return this;
    }

    public OrderShipmentDepartureCustomsBuilder but() {
        return anOrderShipmentDepartureCustoms().withDeleted(deleted).withId(id).withShipment(shipment).withLastUpdated(lastUpdated).withLastUpdatedBy(lastUpdatedBy).withCustomsAgent(customsAgent).withDeletedAt(deletedAt).withCustomsAgentLocation(customsAgentLocation).withCustomsOffice(customsOffice);
    }

    public OrderShipmentDepartureCustoms build() {
        OrderShipmentDepartureCustoms orderShipmentDepartureCustoms = new OrderShipmentDepartureCustoms();
        orderShipmentDepartureCustoms.setDeleted(deleted);
        orderShipmentDepartureCustoms.setId(id);
        orderShipmentDepartureCustoms.setShipment(shipment);
        orderShipmentDepartureCustoms.setLastUpdated(lastUpdated);
        orderShipmentDepartureCustoms.setLastUpdatedBy(lastUpdatedBy);
        orderShipmentDepartureCustoms.setCustomsAgent(customsAgent);
        orderShipmentDepartureCustoms.setDeletedAt(deletedAt);
        orderShipmentDepartureCustoms.setCustomsAgentLocation(customsAgentLocation);
        orderShipmentDepartureCustoms.setCustomsOffice(customsOffice);
        return orderShipmentDepartureCustoms;
    }
}
