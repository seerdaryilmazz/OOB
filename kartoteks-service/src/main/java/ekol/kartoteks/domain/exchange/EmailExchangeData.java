package ekol.kartoteks.domain.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.kartoteks.domain.EmailWithType;

/**
 * Created by kilimci on 05/05/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailExchangeData {

    private String usageType;
    private String email;

    public static EmailExchangeData fromEmail(EmailWithType emailWithType){
        EmailExchangeData exchangeData = new EmailExchangeData();
        exchangeData.setEmail(emailWithType.getEmail().toString());
        exchangeData.setUsageType(emailWithType.getUsageType() != null ? emailWithType.getUsageType().getCode() : null);
        return exchangeData;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
