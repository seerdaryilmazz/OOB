package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.PermissionType;
import ekol.orders.transportOrder.domain.RouteRequirement;
import ekol.orders.transportOrder.domain.RouteRequirementRoute;
import ekol.orders.transportOrder.domain.TransportOrder;
import ekol.orders.transportOrder.domain.TransportType;

import java.util.HashSet;
import java.util.Set;

public final class RouteRequirementBuilder {

    private Long id;
    private TransportOrder transportOrder;
    private PermissionType permissionType;
    private TransportType transportType;
    private Set<RouteRequirementRoute> routes = new HashSet<>();
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private RouteRequirementBuilder() {
    }

    public static RouteRequirementBuilder aRouteRequirement() {
        return new RouteRequirementBuilder();
    }

    public RouteRequirementBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RouteRequirementBuilder withTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
        return this;
    }

    public RouteRequirementBuilder withPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
        return this;
    }

    public RouteRequirementBuilder withTransportType(TransportType transportType) {
        this.transportType = transportType;
        return this;
    }

    public RouteRequirementBuilder withRoutes(Set<RouteRequirementRoute> routes) {
        this.routes = routes;
        return this;
    }

    public RouteRequirementBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public RouteRequirementBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public RouteRequirementBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public RouteRequirementBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public RouteRequirement build() {
        RouteRequirement routeRequirement = new RouteRequirement();
        routeRequirement.setId(id);
        routeRequirement.setTransportOrder(transportOrder);
        routeRequirement.setPermissionType(permissionType);
        routeRequirement.setTransportType(transportType);
        routeRequirement.setRoutes(routes);
        routeRequirement.setDeleted(deleted);
        routeRequirement.setDeletedAt(deletedAt);
        routeRequirement.setLastUpdated(lastUpdated);
        routeRequirement.setLastUpdatedBy(lastUpdatedBy);
        return routeRequirement;
    }
}
