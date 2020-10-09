package ekol.resource.swagger;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.*;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import springfox.documentation.swagger.web.SecurityConfiguration;

@ControllerAdvice("springfox.documentation.swagger.web")
public class SwaggerSecurityResponseModifier implements ResponseBodyAdvice<Object> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerSecurityResponseModifier.class);
	
	private static final String XSRF_TOKEN_HEADER_PARAM_NAME = "X-XSRF-TOKEN";
	private static final String API_KEY_VALUE_FIELD_NAME = "apiKey";
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		if(body instanceof SecurityConfiguration){
			try {
				SecurityConfiguration config = SecurityConfiguration.class.cast(body);
				if(StringUtils.equalsIgnoreCase(config.getApiKeyName(), XSRF_TOKEN_HEADER_PARAM_NAME)){
					String csrfToken = ((ServletServerHttpRequest)request).getServletRequest().getHeader(XSRF_TOKEN_HEADER_PARAM_NAME);
					Field field = ReflectionUtils.findField(SecurityConfiguration.class, API_KEY_VALUE_FIELD_NAME);
					field.setAccessible(true);
					field.set(config, csrfToken);
				}						
			} catch (Exception e) {
				LOGGER.info("Failed to add CSRF token on Swagger", e);
			}
		}		
		return body;
	}
}
