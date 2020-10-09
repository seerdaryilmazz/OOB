package ekol.model;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.*;

import com.fasterxml.jackson.annotation.*;

import ekol.exceptions.ApplicationException;
import lombok.*;

/**
 * Created by burak on 27/01/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdNamePair implements Serializable {

    private Long id;
    private String name;

    public static IdNamePair with(Long id, String name){
        return new IdNamePair(id, name);
    }

    public static IdNamePair fromMap(Map<String, Object> map){
        Long id;
        String name;

        Object idObject = map.get("id");
        Object nameObject = map.get("name");

        if(idObject instanceof Integer) {
            id = Long.valueOf(Integer.class.cast(idObject));
        } else if(idObject instanceof Long) {
            id = Long.class.cast(idObject);
        } else {
            throw new ApplicationException("Invalid Id Type");
        }

        if(nameObject instanceof String) {
            name = String.class.cast(nameObject);
        } else {
            throw new ApplicationException("Invalid Name Type");
        }


        return new IdNamePair(id, name);
    }

    @JsonIgnore
    public boolean isValid() {
    	return StringUtils.isNotBlank(getName());
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

        IdNamePair pair = IdNamePair.class.cast(object);
        return new EqualsBuilder().
                append(getId(), pair.getId()).
                append(getName(), pair.getName()).
                isEquals();
    }
}
