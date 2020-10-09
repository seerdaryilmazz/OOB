package ekol.crm.quote.queue.exportq.connection;

import java.util.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalSystemConnectionProperties {
	private Map<String, String> exportRules = new HashMap<>();
	
    private String endpoint;
    private String username;
    private String password;
    private int connectTimeout = 30000;
    private int readTimeout = 90000;
}
