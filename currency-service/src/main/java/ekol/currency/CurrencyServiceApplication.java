package ekol.currency;


import ekol.event.annotation.OneOrderEnableEvents;
import ekol.hibernate5.config.OneOrderDatabaseApplication;
import ekol.resource.OneOrderResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@OneOrderResourceServer
@OneOrderDatabaseApplication
@OneOrderEnableEvents
@EnableScheduling
public class CurrencyServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CurrencyServiceApplication.class);
        app.run(args);
    }
}
