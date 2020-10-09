package ekol.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import ekol.event.exception.EventException;
import ekol.event.model.EventWithContext;
import ekol.event.monitoring.EventMonitoring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by kilimci on 04/12/2017.
 */
@Component
public class ReflectionConsumer {

    @Autowired
    private EventMonitoring eventMonitoring;

    public void consume(HandlerMethod handlerMethod, Object controller, EventWithContext eventWithContext){
        String message = (String) eventWithContext.getEvent().getData();
        Method method = handlerMethod.getMethod();
        if (method.getParameterTypes() == null) {
            throw new IllegalArgumentException("Consumer method parameter types are null");
        }
        if(method.getParameterCount() != 1){
            throw new IllegalArgumentException("Consumer method must accept one parameter");
        }
        try {
            Class<?> parameterClass = method.getParameterTypes()[0];
            Object expectedMessage = new ObjectMapper().readValue(message, parameterClass);
            Object[] params = new Object[] {expectedMessage};
            method.invoke(controller, params);
        } catch (IOException | IllegalAccessException | InvocationTargetException e){
            throw new EventException(e);
        }
    }
}
