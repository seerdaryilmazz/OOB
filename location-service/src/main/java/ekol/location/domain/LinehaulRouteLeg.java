package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "LinehaulRouteLeg.allDetails",
                attributeNodes = {
                        @NamedAttributeNode(value = "from"),
                        @NamedAttributeNode(value = "to"),
                        @NamedAttributeNode(value = "schedules"),
                        @NamedAttributeNode(value = "nonSchedules")
                }
        )
})
@Entity
@Table(name = "LhaulRouteLeg")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinehaulRouteLeg extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_lhaul_route_leg", sequenceName = "seq_lhaul_route_leg")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lhaul_route_leg")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RouteLegType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromId")
    private LinehaulRouteLegStop from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toId")
    private LinehaulRouteLegStop to;

    @OneToMany(mappedBy = "parent")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<LinehaulRouteLegSchedule> schedules = new HashSet<>();

    @OneToMany(mappedBy = "parent")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<LinehaulRouteLegNonSchedule> nonSchedules = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RouteLegType getType() {
        return type;
    }

    public void setType(RouteLegType type) {
        this.type = type;
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

    public Set<LinehaulRouteLegSchedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<LinehaulRouteLegSchedule> schedules) {
        this.schedules = schedules;
    }

    public Set<LinehaulRouteLegNonSchedule> getNonSchedules() {
        return nonSchedules;
    }

    public void setNonSchedules(Set<LinehaulRouteLegNonSchedule> nonSchedules) {
        this.nonSchedules = nonSchedules;
    }

    public String getDescription() {
        return (from == null ? "N/A" : from.getName()) + " - " + (to == null ? "N/A" : to.getName());
    }
}
