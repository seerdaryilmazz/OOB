package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by kilimci on 13/09/2017.
 */
@Entity
@Table(name = "ShipmentBarcode")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentBarcode extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_shipment_barcode", sequenceName = "seq_shipment_barcode")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_shipment_barcode")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentId")
    @JsonBackReference
    private Shipment shipment;

    private Integer indexNo;

    private String barcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
