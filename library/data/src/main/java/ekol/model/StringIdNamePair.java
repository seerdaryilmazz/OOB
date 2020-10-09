package ekol.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StringIdNamePair implements Serializable {

    private String id;
    private String name;

    public static StringIdNamePair with(String id, String name){
        return new StringIdNamePair(id, name);
    }

    @JsonIgnore
    public boolean isValid() {
        return StringUtils.isNotBlank(getId()) && StringUtils.isNotBlank(getName());
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
        if (!(object instanceof StringIdNamePair))
            return false;
        if (object == this)
            return true;

        StringIdNamePair pair = StringIdNamePair.class.cast(object);
        return new EqualsBuilder().
                append(getId(), pair.getId()).
                append(getName(), pair.getName()).
                isEquals();
    }
}
