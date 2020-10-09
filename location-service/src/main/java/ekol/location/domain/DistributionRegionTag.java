package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "DistRgTag")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class DistributionRegionTag extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_dist_rg_tag", sequenceName = "seq_dist_rg_tag")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_dist_rg_tag")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distributionRegionId")
    @JsonBackReference
    private DistributionRegion distributionRegion;

    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DistributionRegion getDistributionRegion() {
        return distributionRegion;
    }

    public void setDistributionRegion(DistributionRegion distributionRegion) {
        this.distributionRegion = distributionRegion;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
