package ekol.crm.activity;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

import ekol.appMonitoring.annotation.OneOrderEnableAppMonitoring;
import ekol.event.annotation.OneOrderEnableEvents;
import ekol.hibernate5.config.OneOrderDatabaseApplication;
import ekol.mongodb.config.OneOrderMongoApplication;
import ekol.notification.annotation.OneOrderEnableNotification;
import ekol.resource.OneOrderResourceServer;


@EnableEurekaClient
@SpringBootApplication
@OneOrderResourceServer
@OneOrderMongoApplication
@OneOrderDatabaseApplication
@OneOrderEnableEvents
@EnableAsync
@OneOrderEnableNotification
@OneOrderEnableAppMonitoring
public class ActivityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivityServiceApplication.class, args);
    }
}
