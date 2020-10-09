package ekol.event.monitoring;

import ekol.event.annotation.ConsumesWebEvent;
import ekol.event.annotation.ProducesEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kilimci on 28/11/2017.
 */
public class RegisterEventMessage {

    private String applicationName;
    private List<ConsumedEvent> consumes = new ArrayList<>();
    private List<ProducedEvent> produces = new ArrayList<>();

    public static RegisterEventMessage createWith(String applicationName, List<ProducesEvent> producesEvents, List<ConsumesWebEvent> consumesEvents){
        RegisterEventMessage message = new RegisterEventMessage();
        message.setApplicationName(applicationName);
        message.setConsumes(
                consumesEvents.stream().map(ConsumedEvent::createWith)
                        .collect(Collectors.toList()));
        message.setProduces(
                producesEvents.stream().map(ProducedEvent::createWith)
                        .collect(Collectors.toList()));

        return message;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public List<ConsumedEvent> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<ConsumedEvent> consumes) {
        this.consumes = consumes;
    }

    public List<ProducedEvent> getProduces() {
        return produces;
    }

    public void setProduces(List<ProducedEvent> produces) {
        this.produces = produces;
    }

    public static class ProducedEvent{
        private String eventName;

        public static ProducedEvent createWith(ProducesEvent producesEvent){
            ProducedEvent event = new ProducedEvent();
            event.setEventName(producesEvent.event());
            return event;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }
    }
    public static class ConsumedEvent{
        private String eventName;
        private String queueName;

        public static ConsumedEvent createWith(ConsumesWebEvent consumesEvent){
            ConsumedEvent event = new ConsumedEvent();
            event.setEventName(consumesEvent.event());
            event.setQueueName(consumesEvent.name());
            return event;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getQueueName() {
            return queueName;
        }

        public void setQueueName(String queueName) {
            this.queueName = queueName;
        }
    }
}
