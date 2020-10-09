package ekol.resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by ozer on 22/02/2017.
 */
public class RequestContextFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String refreshToken = httpServletRequest.getHeader("x-refresh-token");
            if (StringUtils.isNotBlank(refreshToken)) {
                RequestContext.getCurrentContext().setRefreshToken(refreshToken);
            }
        }

        chain.doFilter(request, response);
        RequestContext.unset();
    }
}
