package ekol.kartoteks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import ekol.annotation.OneOrderEnableCache;
import ekol.appMonitoring.annotation.OneOrderEnableAppMonitoring;
import ekol.event.annotation.OneOrderEnableEvents;
import ekol.hibernate5.config.OneOrderDatabaseApplication;
import ekol.kartoteks.domain.export.connection.ExternalSystemConnectionProperties;
import ekol.resource.OneOrderResourceServer;

/**
 * Created by kilimci on 21/07/16.
 */
@SpringBootApplication
@EnableEurekaClient
@OneOrderResourceServer
@OneOrderDatabaseApplication
@EnableScheduling
@OneOrderEnableEvents
@OneOrderEnableAppMonitoring
@OneOrderEnableCache
public class KartoteksServiceApplication {

    @Bean
    @ConfigurationProperties(prefix="boomi.quadro")
    public ExternalSystemConnectionProperties quadroConnectionProperties(){
        return new ExternalSystemConnectionProperties();
    }
    @Bean
    @ConfigurationProperties(prefix="boomi.salesforce")
    public ExternalSystemConnectionProperties salesforceConnectionProperties(){
        return new ExternalSystemConnectionProperties();
    }
    @Bean
    @ConfigurationProperties(prefix="boomi.qlite")
    public ExternalSystemConnectionProperties qliteConnectionProperties(){
        return new ExternalSystemConnectionProperties();
    }

    public static void main(String[] args) {
        SpringApplication.run(KartoteksServiceApplication.class, args);
    }


}
