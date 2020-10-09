package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "LhaulRouteLegNonSchdl")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinehaulRouteLegNonSchedule extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_lhaul_route_leg_non_schdl", sequenceName = "seq_lhaul_route_leg_non_schdl")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lhaul_route_leg_non_schdl")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    @JsonBackReference
    private LinehaulRouteLeg parent;

    private Long companyId;

    private String companyName;

    /**
     * Yol kaç dakika sürüyor? Başka deyişle kalkış zamanı ile varış zamanı arasındaki süre kaç dakika?
     */
    private Integer duration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LinehaulRouteLeg getParent() {
        return parent;
    }

    public void setParent(LinehaulRouteLeg parent) {
        this.parent = parent;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
