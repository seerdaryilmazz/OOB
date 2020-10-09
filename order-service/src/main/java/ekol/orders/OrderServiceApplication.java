package ekol.orders;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

import ekol.appMonitoring.annotation.OneOrderEnableAppMonitoring;
import ekol.event.annotation.OneOrderEnableEvents;
import ekol.hibernate5.config.OneOrderDatabaseApplication;
import ekol.resource.OneOrderResourceServer;

/**
 * Created by kilimci on 18/07/16.
 */
@EnableEurekaClient
@SpringBootApplication
@OneOrderDatabaseApplication
@OneOrderResourceServer
@EnableScheduling
@OneOrderEnableEvents
@OneOrderEnableAppMonitoring
public class OrderServiceApplication {

    public static void main(String[] args) {
        new SpringApplication(OrderServiceApplication.class).run(args);
    }
}
