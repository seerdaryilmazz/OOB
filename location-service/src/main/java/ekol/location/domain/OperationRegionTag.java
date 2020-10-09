package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "OperationRgTag")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class OperationRegionTag extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_operation_rg_tag", sequenceName = "seq_operation_rg_tag")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_operation_rg_tag")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operationRegionId")
    @JsonBackReference
    private OperationRegion operationRegion;

    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationRegion getOperationRegion() {
        return operationRegion;
    }

    public void setOperationRegion(OperationRegion operationRegion) {
        this.operationRegion = operationRegion;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
