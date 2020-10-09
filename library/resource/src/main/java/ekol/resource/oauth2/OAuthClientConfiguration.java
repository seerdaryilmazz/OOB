package ekol.resource.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * Created by kilimci on 16/07/16.
 */
@Configuration
@EnableOAuth2Client
public class OAuthClientConfiguration {

    @Value("${security.oauth2.client.userAuthorizationUri}")
    private String authorizeUrl;

    @Value("${security.oauth2.client.accessTokenUri}")
    private String tokenUrl;

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    /**
     * RestTempate that relays the OAuth2 token passed to the task webservice.
     *
     * @param oauth2ClientContext
     * @return
     */
    @Bean
    public OAuth2RestTemplate restTemplate(RestTemplateCustomizer customizer, OAuth2ClientContext oauth2ClientContext) {

        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource(), oauth2ClientContext);

        MappingJackson2HttpMessageConverter jacksonMessageConverterOfRestTemplate = null;

        if (restTemplate.getMessageConverters() != null) {
            for (HttpMessageConverter<?> converter : restTemplate.getMessageConverters()) {
                if (converter instanceof MappingJackson2HttpMessageConverter) {
                    jacksonMessageConverterOfRestTemplate = (MappingJackson2HttpMessageConverter) converter;
                    break;
                }
            }
        }

        if (jacksonMessageConverterOfRestTemplate != null) {
            restTemplate.getMessageConverters().remove(jacksonMessageConverterOfRestTemplate);
        }

        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);

        customizer.customize(restTemplate);

        return restTemplate;
    }

    /**
     * Setup details where the OAuth2 server is.
     *
     * @return
     */
    @Bean
    protected OAuth2ProtectedResourceDetails resource() {
        AuthorizationCodeResourceDetails resource = new AuthorizationCodeResourceDetails();
        resource.setAccessTokenUri(tokenUrl);
        resource.setUserAuthorizationUri(authorizeUrl);
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        return resource;
    }
}
