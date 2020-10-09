package ekol.mongodb.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * Created by burak on 30/05/17.
 */
@Configuration
@EnableMongoAuditing
@EntityScan(basePackages = "ekol")
public class AuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider(){
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }
            return (String)((OAuth2Authentication) authentication).getUserAuthentication().getPrincipal();
        };
    }
}
