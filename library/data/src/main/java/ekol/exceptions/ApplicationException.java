package ekol.exceptions;

/**
 * Created by kilimci on 09/09/16.
 */
public class ApplicationException extends ParameterizedRuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(String message, Object... args) {
        super(message, args);
    }
}
