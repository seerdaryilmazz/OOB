package ekol.location.domain.dto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdTypeName  {

    private Long id;
    private String type;
    private String name;

    public static IdTypeName with(Long id, String type, String name){
        return new IdTypeName(id, type, name);
    }

    public IdTypeName() {

    }
    public IdTypeName(Long id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    @JsonIgnore
    public boolean isValid() {
        return !(getId() == null || StringUtils.isEmpty(getType()) || StringUtils.isEmpty(getName()));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 23).
                append(getId()).
                append(getType()).
                append(getName()).
                toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IdTypeName))
            return false;
        if (object == this)
            return true;

        IdTypeName pair = (IdTypeName) object;
        return new EqualsBuilder().
                append(getId(), pair.getId()).
                append(getType(), pair.getType()).
                append(getName(), pair.getName()).
                isEquals();
    }
}
