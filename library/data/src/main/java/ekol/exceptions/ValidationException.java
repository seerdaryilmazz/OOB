package ekol.exceptions;

/**
 * Created by kilimci on 10/06/16.
 */
public class ValidationException extends ParameterizedRuntimeException {

    public ValidationException() {
        super("Validation error");
    }
    public ValidationException(String message) {
        super(message);
    }
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
    public ValidationException(String message, Object... args) {
        super(message, args);
    }

}
