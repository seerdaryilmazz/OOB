package ekol.agreement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dogukan Sahinturk on 21.10.2019
 */
@Data
@Configuration
@ConfigurationProperties(prefix="oneorder.notification.export-company")
public class ExportCompanyNotificationMailList {
    private List<String> tr = new ArrayList<>();
    private List<String> other = new ArrayList<>();
}
