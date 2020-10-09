package ekol.event.component;

import ekol.event.annotation.ConsumesWebEvent;
import ekol.event.annotation.ProducesEvent;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kilimci on 28/11/2017.
 */
@Component
public class BeansWithEventResolver {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    public List<ProducesEvent> findProducedEvents(ApplicationContext applicationContext) {
        Map<String, Object> components = applicationContext.getBeansWithAnnotation(Component.class);
        List<ProducesEvent> producedEvents = new ArrayList<>();
        components.keySet().forEach((beanName) -> {
            Method[] methods = AopUtils.getTargetClass(applicationContext.getBean(beanName)).getDeclaredMethods();
            producedEvents.addAll(
                    Arrays.stream(methods).filter(method -> method.isAnnotationPresent(ProducesEvent.class))
                            .map(method -> method.getAnnotation(ProducesEvent.class))
                            .collect(Collectors.toList())
            );
        });

        return producedEvents;
    }
    public List<ConsumesWebEvent> findConsumedEvents() {

        List<ConsumesWebEvent> consumedEvents = new ArrayList<>();
        handlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            if (handlerMethod.getMethod().isAnnotationPresent(ConsumesWebEvent.class)) {
                ConsumesWebEvent consumesEvent = handlerMethod.getMethodAnnotation(ConsumesWebEvent.class);
                consumedEvents.add(consumesEvent);
            }
        });

        return consumedEvents;
    }
}
