package ekol.authorization.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.authorization.domain.auth.AuthOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 26/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationRequest {
    AuthOperation operation = new AuthOperation();
    List<AuthorizedRelation> authorizations = new ArrayList<>();

    public AuthOperation getOperation() {
        return operation;
    }

    public void setOperation(AuthOperation operation) {
        this.operation = operation;
    }

    public List<AuthorizedRelation> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<AuthorizedRelation> authorizations) {
        this.authorizations = authorizations;
    }
}
