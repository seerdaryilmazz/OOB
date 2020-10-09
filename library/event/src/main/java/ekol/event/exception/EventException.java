package ekol.event.exception;

/**
 * Created by ozer on 25/10/16.
 */
public class EventException extends RuntimeException {

    public EventException(String cause) {
        super(cause);
    }
    public EventException(Throwable cause) {
        super(cause);
    }
}
