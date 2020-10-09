package ekol.crm.quote.queue;

import ekol.crm.quote.queue.exportq.connection.ExternalSystemConnectionProperties;
import ekol.event.annotation.OneOrderEnableEvents;
import ekol.mongodb.config.OneOrderMongoApplication;
import ekol.oneorder.configuration.OneOrderEnableConfiguration;
import ekol.resource.OneOrderResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

@EnableEurekaClient
@SpringBootApplication
@OneOrderResourceServer
@OneOrderMongoApplication
@OneOrderEnableEvents
@EnableRetry
@OneOrderEnableConfiguration
public class QuoteQueueServiceApplication {
	
    @Bean
    @ConfigurationProperties(prefix="boomi.quadro")
    public ExternalSystemConnectionProperties quadroConnectionProperties(){
        return new ExternalSystemConnectionProperties();
    }
    
	public static void main(String[] args) {
		SpringApplication.run(QuoteQueueServiceApplication.class, args);
	}

}
