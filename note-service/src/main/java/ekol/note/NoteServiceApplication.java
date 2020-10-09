package ekol.note;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import ekol.event.annotation.OneOrderEnableEvents;
import ekol.mongodb.config.OneOrderMongoApplication;
import ekol.resource.OneOrderResourceServer;


@EnableEurekaClient
@SpringBootApplication
@OneOrderResourceServer
@OneOrderMongoApplication
@OneOrderEnableEvents
public class NoteServiceApplication {

    public static void main(String[] args) {
    	SpringApplication.run(NoteServiceApplication.class, args);
    }
}
