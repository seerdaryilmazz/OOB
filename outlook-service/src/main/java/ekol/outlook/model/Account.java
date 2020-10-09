package ekol.outlook.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account{
    private String id;
    private String username;
    private String tenantId;
    private String mailAddress;
    private TokenHolder tokenHolder;
}
