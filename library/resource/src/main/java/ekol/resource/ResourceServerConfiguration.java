package ekol.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

/**
 * Created by kilimci on 21/07/16.
 */
@Configuration
@EnableResourceServer
@ComponentScan
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${oneorder.jwt-key}")
    private String jwtKey;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().and().addFilterAfter(new RequestContextFilter(), SwitchUserFilter.class);
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resourceServerSecurityConfigurer) {
        resourceServerSecurityConfigurer.resourceId(applicationName);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtKey);
        return converter;
    }
}