package ekol.crm.quote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

import ekol.annotation.OneOrderEnableCache;
import ekol.appMonitoring.annotation.OneOrderEnableAppMonitoring;
import ekol.event.annotation.OneOrderEnableEvents;
import ekol.hibernate5.config.OneOrderDatabaseApplication;
import ekol.notification.annotation.OneOrderEnableNotification;
import ekol.oneorder.configuration.OneOrderEnableConfiguration;
import ekol.resource.OneOrderResourceServer;

@SpringBootApplication
@EnableEurekaClient
@OneOrderResourceServer
@OneOrderDatabaseApplication
@OneOrderEnableEvents
@OneOrderEnableAppMonitoring
@EnableAsync
@OneOrderEnableNotification
@OneOrderEnableCache
@OneOrderEnableConfiguration
public class QuoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuoteApplication.class, args);
	}
}
