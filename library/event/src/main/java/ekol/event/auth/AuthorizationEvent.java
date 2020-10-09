package ekol.event.auth;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ozer on 27/02/2017.
 */
public class AuthorizationEvent {

    public static class AuthorizationEventItem {

        private Set<String> patterns;
        private List<String> operations;
        private Set<RequestMethod> methods;

        public AuthorizationEventItem() {
        }

        public AuthorizationEventItem(Set<String> patterns, List<String> operations, Set<RequestMethod> methods) {
            this.patterns = patterns;
            this.operations = operations;
            this.methods = methods;
        }

        public Set<String> getPatterns() {
            return patterns;
        }

        public void setPatterns(Set<String> patterns) {
            this.patterns = patterns;
        }

        public List<String> getOperations() {
            return operations;
        }

        public void setOperations(List<String> operations) {
            this.operations = operations;
        }

        public Set<RequestMethod> getMethods() {
            return methods;
        }

        public void setMethods(Set<RequestMethod> methods) {
            this.methods = methods;
        }
    }

    private String serviceName;
    private List<AuthorizationEventItem> authorizationEventItems = new ArrayList<>();

    public AuthorizationEvent() {
    }

    public AuthorizationEvent(String serviceName, List<AuthorizationEventItem> authorizationEventItems) {
        this.serviceName = serviceName;
        this.authorizationEventItems = authorizationEventItems;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<AuthorizationEventItem> getAuthorizationEventItems() {
        return authorizationEventItems;
    }

    public void setAuthorizationEventItems(List<AuthorizationEventItem> authorizationEventItems) {
        this.authorizationEventItems = authorizationEventItems;
    }

    public void addAuthorizationEventItem(Set<String> patterns, List<String> operations, Set<RequestMethod> methods) {
        AuthorizationEventItem authorizationEventItem = new AuthorizationEventItem();
        authorizationEventItem.setPatterns(patterns);
        authorizationEventItem.setOperations(operations);
        authorizationEventItem.setMethods(methods);
        this.getAuthorizationEventItems().add(authorizationEventItem);
    }
}
