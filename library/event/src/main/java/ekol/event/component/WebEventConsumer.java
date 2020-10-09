package ekol.event.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import ekol.event.consumer.OAuth2Consumer;
import ekol.event.consumer.ReflectionConsumer;
import ekol.event.model.EventWithContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by kilimci on 15/08/2017.
 */
public class WebEventConsumer extends DefaultConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(WebEventConsumer.class);

    private String queueName;
    private OAuth2Consumer oAuth2Consumer;
    private ReflectionConsumer reflectionConsumer;
    private WebEventUrl url;
    private HandlerMethod handlerMethod;
    private Object controller;

    public WebEventConsumer(Channel channel, String queueName, WebEventUrl url, HandlerMethod handlerMethod, Object controller){
        super(channel);
        this.queueName = queueName;
        this.url = url;
        this.handlerMethod = handlerMethod;
        this.controller = controller;
    }

    public void setOAuth2Consumer(OAuth2Consumer oAuth2Consumer) {
        this.oAuth2Consumer = oAuth2Consumer;
    }

    public void setReflectionConsumer(ReflectionConsumer reflectionConsumer) {
        this.reflectionConsumer = reflectionConsumer;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, StandardCharsets.UTF_8);
        LOG.info("queue: '{}' received message:[{}]", queueName, message);
        try {
            EventWithContext eventWithContext = new ObjectMapper().readValue(message, EventWithContext.class);
            consume(eventWithContext);
            LOG.info("queue '{}' consumed message:[{}]", queueName, message);
            super.getChannel().basicAck(envelope.getDeliveryTag(), false);
        } catch (Exception e) {
            LOG.error(queueName + " exception consuming message:[" + message + "]", e);
            //multiple: false, requeue: true
            //super.getChannel().basicNack(envelope.getDeliveryTag(), false, true);
        }
    }

    private void consume(EventWithContext eventWithContext){
        if(eventWithContext.hasOauth2Token()){
            this.oAuth2Consumer.consume(url, eventWithContext);
        }else{
            this.reflectionConsumer.consume(handlerMethod, controller, eventWithContext);
        }
    }
}
