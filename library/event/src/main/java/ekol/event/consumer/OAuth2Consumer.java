package ekol.event.consumer;

import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import ekol.event.component.*;
import ekol.event.exception.EventException;
import ekol.event.model.EventWithContext;
import ekol.event.monitoring.EventMonitoring;

/**
 * Created by kilimci on 15/08/2017.
 */
@Component
public class OAuth2Consumer {

    @Autowired
    private RestTemplateCustomizer restTemplateCustomizer;

    @Autowired
    private OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails;

    @Autowired
    private TokenValidator tokenValidator;

    @Autowired
    private EventMonitoring eventMonitoring;

    @Value("${spring.application.name}")
    private String springApplicationName;

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2Consumer.class);

    private OAuth2RestTemplate getOAuth2RestTemplate(OAuth2ClientContext oAuth2ClientContext) {
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(oAuth2ProtectedResourceDetails, oAuth2ClientContext);
        restTemplateCustomizer.customize(restTemplate);
        return restTemplate;
    }

    private void validateTokens(EventWithContext eventWithContext){
        tokenValidator.validateTokens(eventWithContext);
        if (eventWithContext.getSecurityContext() != null) {
            SecurityContextHolder.setContext(eventWithContext.getSecurityContext());
        }
    }

    public void consume(WebEventUrl webEventUrl, EventWithContext eventWithContext){
        validateTokens(eventWithContext);

        OAuth2RestTemplate oAuth2RestTemplate = getOAuth2RestTemplate(eventWithContext.getOauth2ClientContext());
        String exception = null;
        boolean retry = false;
        do {
        	retry = false;
        	try {
        		Object body = new ObjectMapper().readValue((String) eventWithContext.getEvent().getData(), Map.class);
        		webEventUrl.resolveUrl(body);
        		MultiValueMap<String, String> headers = new HttpHeaders();
        		headers.add("Authorization", eventWithContext.getOauth2AccessToken().getTokenType() + " " + eventWithContext.getOauth2AccessToken().getValue());
        		Optional.of(eventWithContext.getOauth2AccessToken())
        			.map(OAuth2AccessToken::getRefreshToken)
        			.map(OAuth2RefreshToken::getValue)
        			.ifPresent(refreshToken->headers.add("x-refresh-token", refreshToken));
        		oAuth2RestTemplate.exchange(webEventUrl.getResolvedUrl(), webEventUrl.getHttpMethod(), new HttpEntity<>(body, headers), Map.class, new HashMap<>());
        	} catch (Exception e) {
        		if((e instanceof HttpClientErrorException) && HttpStatus.NOT_FOUND == ((HttpClientErrorException)e).getStatusCode()) {
        			try {
						Thread.sleep(1000L);
					} catch (InterruptedException iex) {
						LOG.error("Error during consume. Thread sleep error: {}", webEventUrl);
						throw new EventException(iex);
					}
        			retry = true;
        			LOG.warn("Error during consume. Retry consume web event: {}", webEventUrl);
        		} else {
        			LOG.error(MessageFormat.format("Error during consume. web event: {0}", webEventUrl), e);
        			exception = ExceptionUtils.getRootCauseMessage(e);
        			throw new EventException(e);
        		}
        	}finally{
        		if(!retry) {
        			eventMonitoring.saveConsumedEvent(oAuth2RestTemplate, eventWithContext, exception);
        		}
        	}
        } while(retry);
    }


}
