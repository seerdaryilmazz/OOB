package ekol.billingitem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "BillingItem.withParent",
                attributeNodes = {
                        @NamedAttributeNode(value = "parent")
                }
        )
})
@Entity
@Table(name = "BillingItem")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class BillingItem extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_billing_item", sequenceName = "seq_billing_item")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_billing_item")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private BillingItemGroup parent;

    private String code;

    private String officialCode;

    private String name;

    @Enumerated(EnumType.STRING)
    private BillingItemType type;

    private Boolean active;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOfficialCode() {
        return officialCode;
    }

    public void setOfficialCode(String officialCode) {
        this.officialCode = officialCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BillingItemType getType() {
        return type;
    }

    public void setType(BillingItemType type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
