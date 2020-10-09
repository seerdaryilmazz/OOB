package ekol.event.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.resource.RequestContext;
import ekol.resource.oauth2.SessionOwner;
import ekol.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by ozer on 26/10/16.
 */
public class EventWithContext {

    private Event event;

    @JsonSerialize(using = EventWithContext.CustomSecurityContextSerializer.class)
    @JsonDeserialize(using = EventWithContext.CustomSecurityContextDeserializer.class)
    private SecurityContext securityContext;

    @JsonIgnore
    private OAuth2AccessToken oauth2AccessToken;

    @JsonIgnore
    private OAuth2ClientContext oauth2ClientContext;

    @JsonIgnore
    private User user;

    public EventWithContext() {
    }

    public EventWithContext(Event event, SecurityContext securityContext) {
        this.event = event;
        this.securityContext = securityContext;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;

        if (securityContext != null && securityContext.getAuthentication() != null) {
            Map<String, String> details = (Map<String, String>) ((OAuth2Authentication) securityContext.getAuthentication()).getUserAuthentication().getDetails();

            DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(details.get("tokenValue"));
            oAuth2AccessToken.setTokenType(details.get("tokenType"));
            oAuth2AccessToken.setRefreshToken(new DefaultOAuth2RefreshToken(details.get("refreshToken")));
            this.oauth2AccessToken = oAuth2AccessToken;

            this.oauth2ClientContext = new DefaultOAuth2ClientContext(oAuth2AccessToken);

            this.user = SessionOwner.getUserFromAccessToken(details.get("tokenValue"));
        }
    }

    public OAuth2AccessToken getOauth2AccessToken() {
        return oauth2AccessToken;
    }

    public OAuth2ClientContext getOauth2ClientContext() {
        return oauth2ClientContext;
    }

    public User getUser() {
        return user;
    }

    public boolean hasOauth2Token(){
        return getOauth2AccessToken() != null;
    }

    public static class CustomSecurityContextSerializer extends JsonSerializer<SecurityContext> {

        @Override
        public void serialize(SecurityContext obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (obj != null && obj.getAuthentication() != null) {
                OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) obj.getAuthentication().getDetails();

                jsonGenerator.writeStartObject();

                jsonGenerator.writeFieldName("principal");
                jsonGenerator.writeString((String) obj.getAuthentication().getPrincipal());

                jsonGenerator.writeFieldName("tokenType");
                jsonGenerator.writeString(oAuth2AuthenticationDetails.getTokenType());

                jsonGenerator.writeFieldName("tokenValue");
                jsonGenerator.writeString(oAuth2AuthenticationDetails.getTokenValue());

                jsonGenerator.writeFieldName("refreshToken");
                jsonGenerator.writeString(RequestContext.getCurrentContext().getRefreshToken());

                jsonGenerator.writeEndObject();
            } else {
                jsonGenerator.writeString((String) null);
            }
        }
    }

    public static class CustomSecurityContextDeserializer extends JsonDeserializer<SecurityContext> {

        @Override
        public SecurityContext deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            return EventWithContext.constructSecurityContext(
                    node.get("principal").asText(),
                    node.get("tokenType").asText(),
                    node.get("tokenValue").asText(),
                    node.get("refreshToken").asText()
            );
        }
    }

    public static SecurityContext constructSecurityContext(String principal, String tokenType, String tokenValue, String refreshToken) {
        Map<String, Object> details = new HashMap<>();
        details.put("tokenType", tokenType);
        details.put("tokenValue", tokenValue);
        details.put("refreshToken", refreshToken);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "", new HashSet<>());
        token.setDetails(details);

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(new OAuth2Request(new HashMap<>(), "", new HashSet<>(), true, new HashSet<>(), new HashSet<>(), "", new HashSet<>(), new HashMap<>()), token);

        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(oAuth2Authentication);

        return securityContext;
    }

    public void updateSecurityContext(String tokenValue, String refreshToken) {
        this.setSecurityContext(constructSecurityContext(getUser().getUsername(), oauth2AccessToken.getTokenType(), tokenValue, refreshToken));
    }
}
