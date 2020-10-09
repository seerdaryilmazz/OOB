package ekol.resource.swagger;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by ozer on 21/10/16.
 */
@Configuration
@ConditionalOnProperty("swagger.enabled")
@EnableSwagger2
public class SwaggerConfiguration extends WebMvcConfigurerAdapter {

    @Value("${spring.application.name}")
    private String applicationName;
    
    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;
    
    @Value("${security.oauth2.client.userAuthorizationUri}")
    private String userAuthorizationUri;
    
    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;
    
    @Value("${security.oauth2.client.scope}")
    private String scope;
    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ekol"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("")
                .protocols(new HashSet<>(Arrays.asList("http","https")))
                .apiInfo(new ApiInfo(
                        applicationName,
                        applicationName,
                        "",
                        "",
                        new Contact("", "", ""),
                        "",
                        "",
                        Collections.emptyList()));
    }
    
    @Bean
	public SecurityConfiguration swaggerSecurityConfiguration() {
		return new SecurityConfiguration(clientId, clientSecret, "One Order",
				"", "{{XSRF_TOKEN_COOKIE}}", ApiKeyVehicle.HEADER, "X-XSRF-TOKEN", ",")  ;
	}
}
