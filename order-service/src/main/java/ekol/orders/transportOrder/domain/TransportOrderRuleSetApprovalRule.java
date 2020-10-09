package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.transportOrder.dto.rule.ApprovalWorkflow;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "TorsApprovalRule") // Tors: Transport Order Rule Set
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportOrderRuleSetApprovalRule extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_tors_load_spec_rule", sequenceName = "seq_tors_load_spec_rule")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tors_load_spec_rule")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    @JsonBackReference
    private TransportOrderRuleSet parent;

    private String name;

    @Enumerated(EnumType.STRING)
    private ApprovalWorkflow workflow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportOrderRuleSet getParent() {
        return parent;
    }

    public void setParent(TransportOrderRuleSet parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApprovalWorkflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(ApprovalWorkflow workflow) {
        this.workflow = workflow;
    }
}
