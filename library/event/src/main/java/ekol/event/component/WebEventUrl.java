package ekol.event.component;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.UriTemplate;

import lombok.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kilimci on 15/08/2017.
 */
@Data
@ToString(includeFieldNames = true)
public class WebEventUrl {

    private String url;
    private String resolvedUrl;
    private HttpMethod httpMethod;
    private Map<String, String> params;


    public static WebEventUrl createWith(String springApplicationName, RequestMappingInfo requestMappingInfo, Map<String, String> params){
        WebEventUrl webEventUrl = new WebEventUrl();
        String firstPattern = requestMappingInfo.getPatternsCondition().getPatterns().iterator().next();
        String url = "http://" + springApplicationName + firstPattern;
        webEventUrl.setUrl(url);
        HttpMethod method = HttpMethod.POST;
        if(!requestMappingInfo.getMethodsCondition().getMethods().contains(RequestMethod.POST)){
            RequestMethod firstRequestMethod = requestMappingInfo.getMethodsCondition().getMethods().iterator().next();
            method = HttpMethod.resolve(firstRequestMethod.name());
        }
        webEventUrl.setHttpMethod(method);
        webEventUrl.setParams(params);
        return webEventUrl;
    }

    public void resolveUrl(Object body){
        if(!getParams().isEmpty()){
            Map<String, Object> resolvedParams = resolveParams(body);
            UriTemplate uriTemplate = new UriTemplate(url);
            URI resolvedUri = uriTemplate.expand(resolvedParams);
            resolvedUrl = resolvedUri.toString();
        }else{
            resolvedUrl = url;
        }

    }
    private Map<String, Object> resolveParams(Object body){
        Map<String, Object> resolved = new HashMap<>();
        ExpressionParser parser = new SpelExpressionParser();
        params.entrySet().forEach(entry -> {
            Expression exp = parser.parseExpression(entry.getValue());
            EvaluationContext context = new StandardEvaluationContext(body);
            resolved.put(entry.getKey(), exp.getValue(context));

        });
        return resolved;
    }
}
