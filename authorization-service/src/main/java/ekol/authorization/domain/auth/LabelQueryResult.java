package ekol.authorization.domain.auth;

import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Collection;

/**
 * Created by ozer on 14/03/2017.
 */
@QueryResult
public class LabelQueryResult {

    private Collection<String> names;

    public Collection<String> getNames() {
        return names;
    }

    public void setNames(Collection<String> names) {
        this.names = names;
    }
}
