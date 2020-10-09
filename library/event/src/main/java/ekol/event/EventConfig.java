package ekol.event;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.*;

/**
 * Created by ozer on 25/10/16.
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan
@EnableRabbit
public class EventConfig {

}
