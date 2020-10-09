package ekol.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import ekol.annotation.OneOrderEnableCache;
import ekol.event.annotation.OneOrderEnableEvents;
import ekol.mongodb.config.OneOrderMongoApplication;
import ekol.resource.OneOrderResourceServer;

@EnableEurekaClient
@SpringBootApplication
@OneOrderResourceServer
@OneOrderMongoApplication
@OneOrderEnableEvents
@OneOrderEnableCache
public class NotificationServiceApplication {
	public static void main(String[] args) {
		new SpringApplication(NotificationServiceApplication.class).run(args);
	}
}
