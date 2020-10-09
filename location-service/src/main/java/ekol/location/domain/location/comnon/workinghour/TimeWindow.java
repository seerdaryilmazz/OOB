package ekol.location.domain.location.comnon.workinghour;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Embeddable;

/**
 * Created by burak on 10/04/17.
 */
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeWindow {

    private String startTime;

    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isValid() {
        if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3, 19).
                append(getStartTime()).
                append(getEndTime()).
                toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TimeWindow))
            return false;
        if (object == this)
            return true;

        TimeWindow pair = (TimeWindow) object;
        return new EqualsBuilder().
                append(getStartTime(), pair.getStartTime()).
                append(getEndTime(), pair.getEndTime()).
                isEquals();
    }


}
