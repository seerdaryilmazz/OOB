package ekol.crm.opportunity;

import ekol.event.annotation.OneOrderEnableEvents;
import ekol.notification.annotation.OneOrderEnableNotification;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import ekol.hibernate5.config.OneOrderDatabaseApplication;
import ekol.resource.OneOrderResourceServer;

@SpringBootApplication
@EnableEurekaClient
@OneOrderResourceServer
@OneOrderDatabaseApplication
@OneOrderEnableEvents
@OneOrderEnableNotification
public class OpportunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpportunityApplication.class, args);
    }
}