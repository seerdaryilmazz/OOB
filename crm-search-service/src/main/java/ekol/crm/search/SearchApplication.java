package ekol.crm.search;

import ekol.appMonitoring.annotation.OneOrderEnableAppMonitoring;
import ekol.event.annotation.OneOrderEnableEvents;
import ekol.hibernate5.config.OneOrderDatabaseApplication;
import ekol.resource.OneOrderResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@OneOrderResourceServer
@OneOrderDatabaseApplication
@OneOrderEnableEvents
@OneOrderEnableAppMonitoring
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SearchApplication.class);
        app.run(args);
    }
}
