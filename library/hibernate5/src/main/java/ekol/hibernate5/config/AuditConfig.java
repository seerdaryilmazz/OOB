package ekol.hibernate5.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * Created by kilimci on 27/04/16.
 */
@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = "ekol")
public class AuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }

            return (String)((OAuth2Authentication) authentication).getUserAuthentication().getPrincipal();
        };
    }
}
