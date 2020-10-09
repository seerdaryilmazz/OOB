package ekol.agreement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import ekol.event.annotation.OneOrderEnableEvents;
import ekol.hibernate5.config.OneOrderDatabaseApplication;
import ekol.notification.annotation.OneOrderEnableNotification;
import ekol.resource.OneOrderResourceServer;

@SpringBootApplication
@EnableEurekaClient
@OneOrderResourceServer
@OneOrderDatabaseApplication
@OneOrderEnableEvents
@OneOrderEnableNotification
public class AgreementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgreementApplication.class, args);
    }
}
