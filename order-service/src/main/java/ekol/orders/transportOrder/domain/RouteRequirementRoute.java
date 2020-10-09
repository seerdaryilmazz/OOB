package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "RouteRequirementRoute")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteRequirementRoute extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_route_requirement_route", sequenceName = "seq_route_requirement_route")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_route_requirement_route")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirementId")
    @JsonBackReference
    private RouteRequirement requirement;

    private Long routeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RouteRequirement getRequirement() {
        return requirement;
    }

    public void setRequirement(RouteRequirement requirement) {
        this.requirement = requirement;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }
}
