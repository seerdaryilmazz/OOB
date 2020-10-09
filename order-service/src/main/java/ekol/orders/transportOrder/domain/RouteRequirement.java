package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.lookup.domain.PermissionType;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "RouteRequirement")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteRequirement extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_route_requirement", sequenceName = "seq_route_requirement")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_route_requirement")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportOrderId")
    @JsonBackReference
    private TransportOrder transportOrder;

    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportTypeId")
    private TransportType transportType;

    @OneToMany(mappedBy = "requirement")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<RouteRequirementRoute> routes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportOrder getTransportOrder() {
        return transportOrder;
    }

    public void setTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public Set<RouteRequirementRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(Set<RouteRequirementRoute> routes) {
        this.routes = routes;
    }
}
