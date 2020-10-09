package ekol.location.domain.location.port;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.location.domain.location.enumeration.PortAssetType;

import javax.persistence.*;

/**
 * Created by burak on 13/04/17.
 */
@Entity
@Table(name="port_asset")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortAsset {

    @Id
    @SequenceGenerator(name = "seq_port_asset", sequenceName = "seq_port_asset")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_port_asset")
    private Long id;

    @Enumerated
    private PortAssetType type;

    private int amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "portId")
    @JsonBackReference
    private Port port;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PortAssetType getType() {
        return type;
    }

    public void setType(PortAssetType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }
}
