package ekol.outlook.model;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TokenHolder {
    @JsonProperty("token_type")
    private String tokenType;
    private String scope;
    @JsonProperty("expires_in")
    private int expiresIn;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("id_token")
    private String idToken;
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;
    @JsonProperty("error_codes")
    private int[] errorCodes;
    private Date expirationTime;
    
    public boolean isValidToken() {
    	return Calendar.getInstance().getTime().before(getExpirationTime());
    }
    
    public boolean hasRefreshToken() {
    	return Optional.ofNullable(refreshToken).isPresent();
    }
}
