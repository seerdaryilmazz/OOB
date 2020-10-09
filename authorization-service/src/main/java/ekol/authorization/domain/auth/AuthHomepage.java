package ekol.authorization.domain.auth;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "Homepage")
public class AuthHomepage extends BaseEntity{

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
