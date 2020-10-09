package ekol.crm.history;


import ekol.event.annotation.OneOrderEnableEvents;
import ekol.mongodb.config.OneOrderMongoApplication;
import ekol.resource.OneOrderResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@OneOrderResourceServer
@OneOrderMongoApplication
@OneOrderEnableEvents
public class HistoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(HistoryServiceApplication.class);
        app.run(args);
    }
}
