package ekol.crm.activity.client.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact {
    private Long id;
    private String fullname;
    private List<EmailType> emails;
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmailType {
    	private Email email;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Email {
    	private String emailAddress;
    }
}
