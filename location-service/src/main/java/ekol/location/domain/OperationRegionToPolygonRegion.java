package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "OperationRgToPolygonRg")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class OperationRegionToPolygonRegion extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_operation_rg_to_polygon_rg", sequenceName = "seq_operation_rg_to_polygon_rg")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_operation_rg_to_polygon_rg")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operationRegionId")
    @JsonBackReference
    private OperationRegion operationRegion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "polygonRegionId")
    private PolygonRegion polygonRegion;

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

    public PolygonRegion getPolygonRegion() {
        return polygonRegion;
    }

    public void setPolygonRegion(PolygonRegion polygonRegion) {
        this.polygonRegion = polygonRegion;
    }
}
