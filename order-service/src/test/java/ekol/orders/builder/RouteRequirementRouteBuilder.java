package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.transportOrder.domain.RouteRequirement;
import ekol.orders.transportOrder.domain.RouteRequirementRoute;

public final class RouteRequirementRouteBuilder {

    private Long id;
    private RouteRequirement requirement;
    private Long routeId;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private RouteRequirementRouteBuilder() {
    }

    public static RouteRequirementRouteBuilder aRouteRequirementRoute() {
        return new RouteRequirementRouteBuilder();
    }

    public RouteRequirementRouteBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RouteRequirementRouteBuilder withRequirement(RouteRequirement requirement) {
        this.requirement = requirement;
        return this;
    }

    public RouteRequirementRouteBuilder withRouteId(Long routeId) {
        this.routeId = routeId;
        return this;
    }

    public RouteRequirementRouteBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public RouteRequirementRouteBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public RouteRequirementRouteBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public RouteRequirementRouteBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public RouteRequirementRoute build() {
        RouteRequirementRoute routeRequirementRoute = new RouteRequirementRoute();
        routeRequirementRoute.setId(id);
        routeRequirementRoute.setRequirement(requirement);
        routeRequirementRoute.setRouteId(routeId);
        routeRequirementRoute.setDeleted(deleted);
        routeRequirementRoute.setDeletedAt(deletedAt);
        routeRequirementRoute.setLastUpdated(lastUpdated);
        routeRequirementRoute.setLastUpdatedBy(lastUpdatedBy);
        return routeRequirementRoute;
    }
}
