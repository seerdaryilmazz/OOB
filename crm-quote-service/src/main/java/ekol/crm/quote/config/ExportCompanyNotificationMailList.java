package ekol.crm.quote.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix="oneorder.notification.export-company")
public class ExportCompanyNotificationMailList {
	private List<String> tr = new ArrayList<>();
	private List<String> other = new ArrayList<>();
}
