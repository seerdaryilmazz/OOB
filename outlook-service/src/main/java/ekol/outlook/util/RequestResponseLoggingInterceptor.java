package ekol.outlook.util;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.*;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.util.StreamUtils;

public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {
    	if(LOGGER.isInfoEnabled()) {
    		LOGGER.info("===========================request begin================================================");
    		LOGGER.info("URI         : {}", request.getURI());
    		LOGGER.info("Method      : {}", request.getMethod());
    		LOGGER.info("Headers     : {}", request.getHeaders());
    		LOGGER.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
    		LOGGER.info("==========================request end================================================");
    	}
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
    	if(LOGGER.isInfoEnabled()) {
    		LOGGER.info("============================response begin==========================================");
    		LOGGER.info("Status code  : {}", response.getStatusCode());
    		LOGGER.info("Status text  : {}", response.getStatusText());
    		LOGGER.info("Headers      : {}", response.getHeaders());
    		LOGGER.info("Response body: {}", StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
    		LOGGER.info("=======================response end=================================================");
    	}
    }
}
