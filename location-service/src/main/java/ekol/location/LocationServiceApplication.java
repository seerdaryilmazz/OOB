package ekol.location;

import ekol.resource.OneOrderResourceServer;
import ekol.event.annotation.OneOrderEnableEvents;
import ekol.hibernate5.config.OneOrderDatabaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by ozer on 01/11/16.
 */
@EnableScheduling
@EnableEurekaClient
@SpringBootApplication
@OneOrderDatabaseApplication
@OneOrderResourceServer
@OneOrderEnableEvents
public class LocationServiceApplication {

    public static void main(String[] args) {
        new SpringApplication(LocationServiceApplication.class).run(args);
    }
}
