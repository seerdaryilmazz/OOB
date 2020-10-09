package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "load_declaration")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadDeclaration extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_load_declaration", sequenceName = "seq_load_declaration")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_load_declaration")
    private Long id;

    @Column
    private Long senderId;

    @Transient
    private Company sender;

    @Column
    private Long consigneeId;

    @Transient
    private Company consignee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Company getSender() {
        return sender;
    }

    public void setSender(Company sender) {
        this.sender = sender;
    }

    public Long getConsigneeId() {
        return consigneeId;
    }

    public void setConsigneeId(Long consigneeId) {
        this.consigneeId = consigneeId;
    }

    public Company getConsignee() {
        return consignee;
    }

    public void setConsignee(Company consignee) {
        this.consignee = consignee;
    }
}
