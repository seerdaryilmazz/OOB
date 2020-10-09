package ekol.authorization.domain.auth;

import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

@Data
@QueryResult
public class AuthLevel {

    private AuthUser user;

    private List<BaseEntity> nodes;

    private int relationshipLength;
}
