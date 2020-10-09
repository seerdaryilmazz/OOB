package ekol.authorization.service;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;

import ekol.authorization.domain.auth.AuthOperation;
import ekol.authorization.domain.dto.OperationUrlCacheItem;
import ekol.authorization.dto.MenuItem;
import ekol.authorization.service.auth.AuthOperationService;
import lombok.AllArgsConstructor;

/**
 * Created by ozer on 28/02/2017.
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizeService {

    private AuthOperationService authOperationService;
    private OperationUrlCacheService operationUrlCacheService;
    private RestTemplate restTemplate;
    private Environment environment;

    private String sanitizeUrl(String url){
        String result = url;
        while(result.contains("//")){
            result = result.replace("//", "/");
        }
        if(!result.startsWith("/")){
            result = "/" + result;
        }
        return result;
    }
    
    public boolean authorizeUi(String url) {
    	String trimmed = StringUtils.removeEnd(StringUtils.removeEnd(url, "#/"), "/");
    	String userService = environment.getProperty("oneorder.user-service");
    	boolean user = flatten(Stream.of(restTemplate.getForObject(userService + "/uimenu/usermenu", MenuItem[].class)).collect(Collectors.toSet()))
    			.parallelStream()
    			.anyMatch(trimmed::equalsIgnoreCase);
    	if(user) {
    		return true;
    	} else {
    		return flatten(Stream.of(restTemplate.getForObject(userService + "/uimenu", MenuItem[].class)).collect(Collectors.toSet()))
    				.parallelStream()
    				.noneMatch(trimmed::equalsIgnoreCase);
    	}
    }
    
    private Set<String> flatten(Set<MenuItem> menu) {
    	Set<String> result = new HashSet<>();
    	if(CollectionUtils.isNotEmpty(menu)) {
    		menu.stream()
    			.map(MenuItem::getUrl)
    			.filter(StringUtils::isNotBlank)
    			.map(t->StringUtils.removeEnd(t, "#/"))
    			.map(t->StringUtils.removeEnd(t, "/"))
    			.forEach(result::add);
    		for (MenuItem menuItem : menu) {
    			result.addAll(flatten(menuItem.getChildren()));
			}
    	}
    	return result;
    }

    public boolean authorize(String url, String method) {
        //TODO: below cases need rethinking.

        Set<String> userShouldBeAllowedOneOf = findOperationsForUrlAndMethod(url, method);
        if (CollectionUtils.isEmpty(userShouldBeAllowedOneOf)) {
            // This url is not secured
            return true;
        }
        if(userShouldBeAllowedOneOf.contains(StringUtils.EMPTY)){
            // This url is bound to no operation
            return true;
        }

        Collection<AuthOperation> userAllowedOperations = authOperationService.myOperations();
        if (CollectionUtils.isEmpty(userAllowedOperations)) {
            // User is not allowed to do any operation
            return false;
        }

        return checkUserAllowedOperations(userShouldBeAllowedOneOf, userAllowedOperations);
    }

    private String resolveServiceName(String url){
        return StringUtils.substringBetween(url, "/", "/");
    }

    private Set<String> findOperationsForUrlAndMethod(String url, String method) {
        String sanitizedUrl = sanitizeUrl(url);
        String serviceName = resolveServiceName(sanitizedUrl);
        if (StringUtils.isBlank(serviceName)) {
            return new HashSet<>();
        }
        operationUrlCacheService.buildCacheIfEmpty(serviceName);
        Set<OperationUrlCacheItem> cache = operationUrlCacheService.getCache(serviceName);
        return findMatchingOperations(cache, serviceName, sanitizedUrl, method);
    }

    private Set<String> findMatchingOperations(Set<OperationUrlCacheItem> cache, String serviceName, String url, String method ){
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        Set<String> operations = new HashSet<>();
        int variableCount = Integer.MAX_VALUE;
        for(OperationUrlCacheItem cacheItem : cache){
            if(cacheItem.getMethod().equals(method)){
                String pattern = "/" + serviceName + cacheItem.getUrl();
                if (antPathMatcher.match(pattern, url)) {
                    Map<String, String> variables = antPathMatcher.extractUriTemplateVariables(pattern, url);
                    if(variables.keySet().size() <= variableCount){
                        variableCount = variables.keySet().size();
                        operations.add(cacheItem.getOperation());
                    }
                }
            }
        }
        return operations;
    }

    private boolean checkUserAllowedOperations(Set<String> userShouldBeAllowedOneOf, Collection<AuthOperation> userAllowedOperations){
        for(String each : userShouldBeAllowedOneOf){
            for(AuthOperation allowedOp : userAllowedOperations){
                if(allowedOp.getName().equalsIgnoreCase(each)){
                    return true;
                }
            }
        }
        return false;
    }
}
