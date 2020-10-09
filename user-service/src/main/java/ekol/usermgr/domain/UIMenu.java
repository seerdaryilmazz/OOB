package ekol.usermgr.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by burak on 15/08/16.
 */
@Entity
@Table(name = "UIMenu")
@SequenceGenerator(name = "SEQ_UIMENU", sequenceName = "SEQ_UIMENU")
@JsonIgnoreProperties(ignoreUnknown = true)
@Where(clause = "deleted = 0")
public class UIMenu extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_UIMENU")
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String url;

    @Column(nullable = false)
    private Integer rank;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties(allowSetters = true, value = {"children", "parent", "relations"})
    private UIMenu parent;

    @OneToMany(mappedBy="parent",fetch = FetchType.LAZY)
    @Where(clause="deleted=0")
    @OrderBy("id ASC")
    @JsonIgnore
    private Set<UIMenu> children = new HashSet<>();

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UIMenu)) {
            return false;
        }
        UIMenu menu = (UIMenu) obj;
        return new EqualsBuilder()
                .append(id, menu.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 39)
                .append(id)
                .toHashCode();
    }


    public void increaseRank(){
        setRank(getRank()+1);
    }
    public void decreaseRank(){
        setRank(getRank()-1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UIMenu getParent() {
        return parent;
    }

    public void setParent(UIMenu parent) {
        this.parent = parent;
    }

    public Set<UIMenu> getChildren() {
        return children;
    }

    public void setChildren(Set<UIMenu> children) {
        this.children = children;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
