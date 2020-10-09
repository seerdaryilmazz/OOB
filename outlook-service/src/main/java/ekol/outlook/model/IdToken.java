package ekol.outlook.model;

import java.util.Base64;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdToken {

    @JsonProperty("exp")
    private long expirationTime;
    @JsonProperty("iat")
    private long notBefore;
    @JsonProperty("sub")
    private String tenantId;
    private String nonce;
    private String name;
    private String email;
    @JsonProperty("oid")
    private String objectId;

    public static synchronized IdToken parseEncodedToken(String encodedToken) throws Exception {
        // Encoded token is in three parts, separated by '.'
        String[] tokenParts = encodedToken.split("\\.");

        // The three parts are: header.token.signature
        String idToken = tokenParts[1];

        return new ObjectMapper().readValue(Base64.getUrlDecoder().decode(idToken), IdToken.class);
    }
}
