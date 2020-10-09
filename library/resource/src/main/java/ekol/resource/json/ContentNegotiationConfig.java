package ekol.resource.json;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by kilimci on 24/07/2017.
 */
@Configuration
public class ContentNegotiationConfig extends WebMvcConfigurerAdapter {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false).defaultContentType(MediaType.APPLICATION_JSON);
    }
}
