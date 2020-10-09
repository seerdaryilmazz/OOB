package ekol.kartoteks.domain.exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ekol.exceptions.ApplicationException;
import ekol.kartoteks.domain.RemoteApplication;

import java.io.IOException;

/**
 * Created by kilimci on 06/06/16.
 */
public class QueueExchangeData {

    private String userName;
    private RemoteApplication application;
    private CompanyExchangeData company;

    public String toJSON(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this) ;
        } catch (JsonProcessingException e) {
            throw new ApplicationException("JSON writing error", e);
        }
    }

    public static QueueExchangeData fromJson(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, QueueExchangeData.class) ;
        } catch (IOException e) {
            throw new ApplicationException("JSON parsing error", e);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public RemoteApplication getApplication() {
        return application;
    }

    public void setApplication(RemoteApplication application) {
        this.application = application;
    }

    public CompanyExchangeData getCompany() {
        return company;
    }

    public void setCompany(CompanyExchangeData company) {
        this.company = company;
    }
}
