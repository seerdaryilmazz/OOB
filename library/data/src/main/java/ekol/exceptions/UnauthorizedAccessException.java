package ekol.exceptions;

/**
 * Created by burak on 10/06/16.
 */
public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException() {
        super("Unauthorized");
    }
    public UnauthorizedAccessException(String message) {
        super(message);
    }
    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }

}