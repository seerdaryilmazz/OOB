package ekol.appMonitoring;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.spring.autoconfigure.MeterRegistryCustomizer;

/**
 * Created by kilimci on 15/12/2017.
 */
@Configuration
public class MonitoringConfiguration {
    private final Logger logger = LoggerFactory.getLogger(MonitoringConfiguration.class);

    private String getHostName(){
        String host = "unknown";
        try {
            ProcessBuilder pb = new ProcessBuilder("hostname");
            Process pr = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            host = in.readLine();
            in.close();
        } catch (IOException e) {
            logger.error("Error getting hostname", e);
        }
        return host;
    }
    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String application) {
        return registry -> registry.config().commonTags("application", application, "host", getHostName());
    }
}
