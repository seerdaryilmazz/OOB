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
public class IdNamePairStr {
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;

    public static IdNamePairStr createWith(String id, String name){
        return new IdNamePairStr(id, name);
    }

    public IdNamePairStr() {

    }
    public IdNamePairStr(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        if (!(object instanceof IdNamePairStr))
            return false;
        if (object == this)
            return true;

        IdNamePairStr pair = (IdNamePairStr) object;
        return new EqualsBuilder().
                append(getId(), pair.getId()).
                append(getName(), pair.getName()).
                isEquals();
    }
}
