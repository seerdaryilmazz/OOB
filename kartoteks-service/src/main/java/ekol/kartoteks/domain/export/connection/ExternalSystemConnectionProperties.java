package ekol.kartoteks.domain.export.connection;

/**
 * Created by kilimci on 12/07/16.
 */
public class ExternalSystemConnectionProperties {

    private String endpoint;
    private String username;
    private String password;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
