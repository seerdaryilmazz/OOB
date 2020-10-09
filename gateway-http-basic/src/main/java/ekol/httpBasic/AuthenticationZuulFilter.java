package ekol.httpBasic;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by kilimci on 16/01/2018.
 */
@Component
public class AuthenticationZuulFilter extends ZuulFilter {

    @Override
    public Object run() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> details = (Map<String, String>)auth.getDetails();

        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader("Authorization", details.get("token_type") + " " + details.get("access_token"));
        ctx.addZuulRequestHeader("x-refresh-token", details.get("refresh_token"));
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}
