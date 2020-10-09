package ekol.crm.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import ekol.annotation.OneOrderEnableCache;
import ekol.appMonitoring.annotation.OneOrderEnableAppMonitoring;
import ekol.event.annotation.OneOrderEnableEvents;
import ekol.hibernate5.config.OneOrderDatabaseApplication;
import ekol.notification.annotation.OneOrderEnableNotification;
import ekol.resource.OneOrderResourceServer;

@SpringBootApplication
@EnableEurekaClient
@OneOrderResourceServer
@OneOrderDatabaseApplication
@OneOrderEnableEvents
@OneOrderEnableAppMonitoring
@OneOrderEnableCache
@OneOrderEnableNotification
public class AccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }
}
