package ekol.event.component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

import org.slf4j.*;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.rabbitmq.client.Channel;

import ekol.event.annotation.*;
import ekol.event.consumer.*;
import ekol.event.exception.EventException;
import ekol.exceptions.ApplicationException;

/**
 * Created by kilimci on 11/08/2017.
 */
@Component
public class WebEventRegisterer {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private OAuth2Consumer oAuth2Consumer;

    @Autowired
    private ReflectionConsumer reflectionConsumer;

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Value("${eureka.client.enabled:true}")
    private boolean eurekaEnabled;

    private static final Logger LOG = LoggerFactory.getLogger(WebEventRegisterer.class);
    private static final String EXCHANGE_TYPE = "fanout";
    private static final String DELAYED_TYPE = "x-delayed-message";

    private void waitForDiscoveryRegistration(){
        List<ServiceInstance> instances = new ArrayList<>();
        while(eurekaEnabled && instances.isEmpty()){
            instances = discoveryClient.getInstances(springApplicationName);
            LOG.info("waiting for at least one instance on eureka: {}", instances.size());
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                throw new ApplicationException("thread interrupted", e);
            }
        }
        LOG.info("{} is reachable with discovery client, registering consumers", springApplicationName);
    }


    public void registerConsumers(ApplicationContext applicationContext) {
        waitForDiscoveryRegistration();

        handlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            if (handlerMethod.getMethod().isAnnotationPresent(ConsumesWebEvent.class)) {
                ConsumesWebEvent consumesEvent = handlerMethod.getMethodAnnotation(ConsumesWebEvent.class);
                WebEventUrl url = WebEventUrl.createWith(springApplicationName, requestMappingInfo, resolveParams(consumesEvent));
                Object controller = handlerMethod.createWithResolvedBean().getBean();
                registerEventConsumer(consumesEvent, url, handlerMethod, controller);
            }
        });
    }

    private Map<String, String> resolveParams(ConsumesWebEvent consumesEvent){
        Map<String, String> params = new HashMap<>();
        for(ConsumesWebEventParam param : consumesEvent.params()){
            params.put(param.key(), param.value());
        }
        return params;
    }

    private Channel createChannel(ConsumesWebEvent consumesEvent) throws IOException, TimeoutException{
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(consumesEvent.transactional());
        String registeredEventName = "oneorder-" + consumesEvent.event();
        if(consumesEvent.delayed()){
            Map<String, Object> args = new HashMap<>();
            args.put("x-delayed-type", EXCHANGE_TYPE);
            channel.exchangeDeclare(registeredEventName, DELAYED_TYPE,false,false, args);
        }else{
            channel.exchangeDeclare(registeredEventName, EXCHANGE_TYPE);
        }

        channel.queueDeclare(consumesEvent.name(), true, false, false, null);
        channel.queueBind(consumesEvent.name(), registeredEventName, "");
        return channel;
    }

    private void registerEventConsumer(ConsumesWebEvent consumesEvent, WebEventUrl url, HandlerMethod handlerMethod, Object controller) {
        try {
            Channel channel = createChannel(consumesEvent);
            WebEventConsumer consumer = new WebEventConsumer(channel, consumesEvent.name(), url, handlerMethod, controller);
            consumer.setOAuth2Consumer(oAuth2Consumer);
            consumer.setReflectionConsumer(reflectionConsumer);
            channel.basicConsume(consumesEvent.name(), false, consumer);
            LOG.info("{} is waiting for messages...", consumesEvent.name());

        } catch (Exception e) {
            throw new EventException(e);
        }
    }
}
