package ekol.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

/**
 * location-service'teki Country referanslarını tutmak amacıyla yapıldı.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IsoNamePair implements Serializable {

    private String iso;
    private String name;

    public static IsoNamePair with(String iso, String name){
        return new IsoNamePair(iso, name);
    }

    @JsonIgnore
    public boolean isValid() {
        return StringUtils.isNotBlank(getIso()) && StringUtils.isNotBlank(getName());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 23).
                append(getIso()).
                append(getName()).
                toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IsoNamePair))
            return false;
        if (object == this)
            return true;

        IsoNamePair pair = IsoNamePair.class.cast(object);
        return new EqualsBuilder().
                append(getIso(), pair.getIso()).
                append(getName(), pair.getName()).
                isEquals();
    }
}
