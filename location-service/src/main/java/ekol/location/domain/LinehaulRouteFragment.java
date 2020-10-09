package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Comparator;

@Entity
@Table(name = "LhaulRouteFrag")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinehaulRouteFragment extends BaseEntity {

    public static final Comparator<LinehaulRouteFragment> FRAGMENT_COMPARATOR = new Comparator<LinehaulRouteFragment>() {
        @Override
        public int compare(LinehaulRouteFragment fragment1, LinehaulRouteFragment fragment2) {
            return fragment1.getOrderNo().compareTo(fragment2.getOrderNo());
        }
    };

    @Id
    @SequenceGenerator(name = "seq_lhaul_route_frag", sequenceName = "seq_lhaul_route_frag")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lhaul_route_frag")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    @JsonBackReference
    private LinehaulRoute parent;

    private Integer orderNo;

    @Enumerated(EnumType.STRING)
    private LinehaulRouteFragmentType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legId")
    private LinehaulRouteLeg leg;

    /**
     * Dikkat: Bu alan bağlı olunan route'u (yani parent route'u) değil, başka bir route'u işaret etmektedir.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routeId")
    private LinehaulRoute route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromId")
    private LinehaulRouteLegStop from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toId")
    private LinehaulRouteLegStop to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LinehaulRoute getParent() {
        return parent;
    }

    public void setParent(LinehaulRoute parent) {
        this.parent = parent;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public LinehaulRouteFragmentType getType() {
        return type;
    }

    public void setType(LinehaulRouteFragmentType type) {
        this.type = type;
    }

    public LinehaulRouteLeg getLeg() {
        return leg;
    }

    public void setLeg(LinehaulRouteLeg leg) {
        this.leg = leg;
    }

    public LinehaulRoute getRoute() {
        return route;
    }

    public void setRoute(LinehaulRoute route) {
        this.route = route;
    }

    public LinehaulRouteLegStop getFrom() {
        return from;
    }

    public void setFrom(LinehaulRouteLegStop from) {
        this.from = from;
    }

    public LinehaulRouteLegStop getTo() {
        return to;
    }

    public void setTo(LinehaulRouteLegStop to) {
        this.to = to;
    }
}
