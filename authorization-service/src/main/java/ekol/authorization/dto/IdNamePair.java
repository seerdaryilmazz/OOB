package ekol.authorization.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

/**
 * Created by burak on 27/01/17.
 */
@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdNamePair {

    private Long id;
    private String name;

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
