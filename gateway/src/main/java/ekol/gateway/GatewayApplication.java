package ekol.gateway;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.csrf.*;
import org.springframework.security.web.header.writers.*;
import org.springframework.security.web.util.matcher.*;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.netflix.zuul.context.RequestContext;

import ekol.appMonitoring.annotation.OneOrderEnableAppMonitoring;
import ekol.resource.oauth2.ConfigOneOrderOAuthClient;

/**
 * Created by kilimci on 26/05/16.
 */
@SpringBootApplication
@EnableZuulProxy
@EnableOAuth2Sso
@EnableEurekaClient
@ConfigOneOrderOAuthClient
@EnableRedisHttpSession
@OneOrderEnableAppMonitoring
public class GatewayApplication extends WebSecurityConfigurerAdapter {

    @Autowired
    private Oauth2LogoutSuccessHandler oauth2LogoutHandler;

    public static void main(String[] args) {
    	SpringApplication.run(GatewayApplication.class, args);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.logout().logoutUrl("/logout")
                .deleteCookies("SESSION","XSRF-TOKEN")
                .logoutSuccessHandler(oauth2LogoutHandler)
                .logoutSuccessUrl("/logout_success")
                .and()
                .authorizeRequests()
                .antMatchers("/**").access("isAuthenticated() && @webAccessControlService.checkAccess(authentication, request)")
                .antMatchers("/login","/static/**","/ui/static/**").permitAll().anyRequest().authenticated()
                .and().csrf().csrfTokenRepository(csrfTokenRepository())
                .ignoringAntMatchers("/outlook-service/outlook/authorize")
                .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
                .addFilterAfter(refreshTokenHeaderFilter(), CsrfFilter.class)

                .headers().cacheControl().disable()
                .addHeaderWriter(new DelegatingRequestMatcherHeaderWriter(
                        new NegatedRequestMatcher(
                                new AntPathRequestMatcher("/ui/**")),
                        new CacheControlHeadersWriter() )
                ).frameOptions().sameOrigin(); // TODO: Frame options için daha kısıtlı (örneğin service ve url bazlı) bir kural tanımlayabilir miyiz?


    }

    private static Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request
                        .getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null
                            || token != null && !token.equals(cookie.getValue())) {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                    RequestContext.getCurrentContext().addZuulRequestHeader("X-XSRF-TOKEN", token);
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    private static CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    private static Filter refreshTokenHeaderFilter() {
        return new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    OAuth2ClientContext oAuth2ClientContext = (OAuth2ClientContext) session.getAttribute("scopedTarget.oauth2ClientContext");
                    if (oAuth2ClientContext != null) {
                        OAuth2AccessToken oAuth2AccessToken = oAuth2ClientContext.getAccessToken();
                        if (oAuth2AccessToken != null && oAuth2AccessToken.getRefreshToken() != null) {
                            RequestContext.getCurrentContext().addZuulRequestHeader("x-refresh-token", oAuth2AccessToken.getRefreshToken().getValue());
                        }
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }
}
