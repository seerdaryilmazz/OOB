package ekol.authorization;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import ekol.appMonitoring.annotation.OneOrderEnableAppMonitoring;
import ekol.event.annotation.OneOrderEnableEvents;
import ekol.hibernate5.config.OneOrderDatabaseApplication;
import ekol.resource.OneOrderResourceServer;

/**
 * Created by kilimci on 26/02/17.
 */
@EnableEurekaClient
@SpringBootApplication
@OneOrderDatabaseApplication
@OneOrderResourceServer
@OneOrderEnableEvents
@OneOrderEnableAppMonitoring
public class AuthorizationServiceApplication extends ResourceServerConfigurerAdapter {

    @Value("${spring.application.name}")
    private String applicationName;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AuthorizationServiceApplication.class);
        app.run(args);
    }

    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user-details/active-memberships").permitAll()
                .anyRequest().authenticated();
    }

    public void configure(ResourceServerSecurityConfigurer resourceServerSecurityConfigurer) throws Exception {
        /**
         *  TODO: This configure should be removed when authentication todos are completed
         */
        resourceServerSecurityConfigurer.resourceId(this.applicationName);
    }

}
