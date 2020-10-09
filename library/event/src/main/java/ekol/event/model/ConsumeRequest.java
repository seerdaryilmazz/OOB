package ekol.event.model;

/**
 * Created by kilimci on 24/11/2017.
 */
public class ConsumeRequest{
    private String consumer;
    private String id;
    private String name;
    private String exception;

    public ConsumeRequest(String consumer, Event event, String exception){
        this.consumer = consumer;
        this.id = event.getId();
        this.name = event.getName();
        this.exception = exception;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}