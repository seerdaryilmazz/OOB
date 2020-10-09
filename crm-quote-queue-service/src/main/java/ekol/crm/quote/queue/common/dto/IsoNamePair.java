package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@JsonIgnoreProperties(ignoreUnknown = true)
public class IsoNamePair {

    private String iso;
    private String name;

    public static IsoNamePair with(String iso, String name){
        return new IsoNamePair(iso, name);
    }

    public IsoNamePair() {

    }

    public IsoNamePair(String iso, String name) {
        this.iso = iso;
        this.name = name;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        IsoNamePair pair = (IsoNamePair) object;
        return new EqualsBuilder().
                append(getIso(), pair.getIso()).
                append(getName(), pair.getName()).
                isEquals();
    }
}
