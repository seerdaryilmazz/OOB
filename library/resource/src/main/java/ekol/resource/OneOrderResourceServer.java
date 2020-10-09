package ekol.resource;

import ekol.resource.json.annotation.ConfigOneOrderContentNegotiation;
import ekol.resource.json.annotation.ConfigOneOrderEnumsJacksonMessageConverter;
import ekol.resource.json.annotation.ConfigOneOrderJavaTypesJacksonMessageConverter;
import ekol.resource.oauth2.ConfigOneOrderOAuthClient;
import ekol.resource.swagger.ConfigOneOrderSwagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kilimci on 21/07/16.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ConfigOneOrderOAuthClient
@ConfigOneOrderResourceServer
@ConfigOneOrderSwagger
@ConfigOneOrderEnumsJacksonMessageConverter
@ConfigOneOrderJavaTypesJacksonMessageConverter
@ConfigOneOrderContentNegotiation
public @interface OneOrderResourceServer {
}
