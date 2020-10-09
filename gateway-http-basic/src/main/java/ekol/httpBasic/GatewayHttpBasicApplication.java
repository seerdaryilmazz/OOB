package ekol.httpBasic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kilimci on 16/01/2018.
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class GatewayHttpBasicApplication extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;

    @Autowired
    private Oauth2AuthenticationProvider authenticationProvider;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GatewayHttpBasicApplication.class);
        app.run(args);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .csrf().disable().authorizeRequests()
                .antMatchers("/**").access("isAuthenticated() && @webAccessControlService.checkAccess(authentication, request)")
                .anyRequest().authenticated()
                .and().httpBasic()
                .authenticationEntryPoint(authEntryPoint);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}
