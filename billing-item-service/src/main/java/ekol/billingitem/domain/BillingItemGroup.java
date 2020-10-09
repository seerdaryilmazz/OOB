package ekol.billingitem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "BillingItemGroup.withParent",
                attributeNodes = {
                        @NamedAttributeNode(value = "parent")
                }
        )
})
@Entity
@Table(name = "BillingItemGroup")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class BillingItemGroup extends BaseEntity implements HasId {

    @Id
    @SequenceGenerator(name = "seq_billing_item_group", sequenceName = "seq_billing_item_group")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_billing_item_group")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private BillingItemGroup parent;

    private String name;

    private Boolean active;

    @Transient
    private Boolean hasChildren;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BillingItemGroup getParent() {
        return parent;
    }

    public void setParent(BillingItemGroup parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
