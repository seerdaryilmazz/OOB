package ekol.orders.transportOrder.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by burak on 27/01/17.
 */
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdNamePair {
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;

    public static IdNamePair createWith(Long id, String name){
        return new IdNamePair(id, name);
    }

    public IdNamePair() {

    }
    public IdNamePair(Long id, String name) {
        this.id = id;
        this.name = name;
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

    @JsonIgnore
    public boolean isValid() {
        if(getId() == null || StringUtils.isEmpty(getName())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 23).
                append(getId()).
                append(getName()).
                toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IdNamePair))
            return false;
        if (object == this)
            return true;

        IdNamePair pair = (IdNamePair) object;
        return new EqualsBuilder().
                append(getId(), pair.getId()).
                append(getName(), pair.getName()).
                isEquals();
    }
}
