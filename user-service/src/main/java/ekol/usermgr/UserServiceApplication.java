package ekol.usermgr;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import ekol.annotation.OneOrderEnableCache;
import ekol.appMonitoring.annotation.OneOrderEnableAppMonitoring;
import ekol.event.annotation.OneOrderEnableEvents;
import ekol.hibernate5.config.OneOrderDatabaseApplication;
import ekol.resource.OneOrderResourceServer;

/**
 * Created by kilimci on 10/06/16.
 */
@EnableEurekaClient
@SpringBootApplication
@OneOrderDatabaseApplication
@OneOrderResourceServer
@OneOrderEnableEvents
@OneOrderEnableCache
@OneOrderEnableAppMonitoring
public class UserServiceApplication extends ResourceServerConfigurerAdapter {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /**
         *  TODO: "/users/checkUsername" & "/users/checkPassword" and "/userdetails/subsidiaries" services shouldn't be accessed without authentication&authorization.
         * This should be fixed when authentication todos are completed
         */
       http.authorizeRequests()
       		.antMatchers("/users/checkUsername", "/users/checkPassword").permitAll()
       		.anyRequest().authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resourceServerSecurityConfigurer) throws Exception {
        resourceServerSecurityConfigurer.resourceId(this.applicationName);
    }

    public static void main(String[] args) {
    	SpringApplication.run(UserServiceApplication.class, args);
    }

}
