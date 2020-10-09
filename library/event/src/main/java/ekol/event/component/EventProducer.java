package ekol.event.component;

import java.io.IOException;
import java.nio.charset.*;
import java.util.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.*;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import ekol.event.annotation.ProducesEvent;
import ekol.event.exception.EventException;
import ekol.event.model.*;
import ekol.event.monitoring.EventMonitoring;

/**
 * Created by ozer on 25/10/16.
 */
@Component
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(EventProducer.class);
    private static final String EXCHANGE_TYPE = "fanout";
    private static final String DELAYED_TYPE = "x-delayed-message";
    private static final Charset MESSAGE_ENCODING = StandardCharsets.UTF_8;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventMonitoring eventMonitoring;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private EventConfigProperties eventConfigProperties;

    public void produce(Event event) {
        if(!eventConfigProperties.isEnabled(event.getName())){
            LOG.info("can not produce event {} is disabled", event.getName());
            return;
        }
        Connection connection = null;
        Channel channel = null;

        try {
            EventWithContext eventWithContext = new EventWithContext(event, SecurityContextHolder.getContext());
            String message = objectMapper.writeValueAsString(eventWithContext);
            String exchangeName = event.getRegisteredName();

            connection = connectionFactory.createConnection();
            channel = connection.createChannel(event.isTransactional());

            if(event.getDelay() > 0){
                declareDelayedExchange(channel, exchangeName);
                publishDelayedMessage(channel, exchangeName, message, event.getDelay());
            }else{
                channel.exchangeDeclare(exchangeName, EXCHANGE_TYPE);
                channel.basicPublish(exchangeName, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(MESSAGE_ENCODING));
            }

            eventMonitoring.saveProducedEvent(eventWithContext);

            LOG.info("Sent message:[{}]", message);
        } catch (Exception e) {
            throw new EventException(e);
        } finally {

            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {
                    // Ignore the exception
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    // Ignore the exception
                }
            }
        }
    }

    private void publishDelayedMessage(Channel channel, String exchangeName, String message, int delay) throws IOException {
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-delay", delay);
        AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder().headers(headers)
                .contentType(MessageProperties.PERSISTENT_TEXT_PLAIN.getContentType())
                .deliveryMode(MessageProperties.PERSISTENT_TEXT_PLAIN.getDeliveryMode());

        channel.basicPublish(exchangeName, "", props.build(), message.getBytes(MESSAGE_ENCODING));
    }

    private void declareDelayedExchange(Channel channel, String exchangeName) throws IOException{
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", EXCHANGE_TYPE);
        channel.exchangeDeclare(exchangeName, DELAYED_TYPE, false, false, args);
    }

    @Pointcut("@annotation(ekol.event.annotation.ProducesEvent)")
    public void producesEvent() { }

    @Around("ekol.event.component.EventProducer.producesEvent()")
    public Object aroundProducesEvent(ProceedingJoinPoint pjp) throws Throwable {
        ProducesEvent producesEvent = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(ProducesEvent.class);
        Object retVal = pjp.proceed();

        if (retVal != null) {
            produce(new Event(applicationName, producesEvent.event(), retVal, producesEvent.delay(), producesEvent.transactional()));
        }

        return retVal;
    }
}
