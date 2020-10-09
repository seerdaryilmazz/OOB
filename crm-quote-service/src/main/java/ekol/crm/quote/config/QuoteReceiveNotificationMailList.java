package ekol.crm.quote.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix="oneorder.notification.quadro-transfer")
public class QuoteReceiveNotificationMailList {
	private List<String> sea = new ArrayList<>();
	private List<String> air = new ArrayList<>();
}
