package ekol.exceptions;

/**
 * Created by kilimci on 10/06/16.
 */
public class BadRequestException extends ParameterizedRuntimeException {

    public BadRequestException() {
        super("Bad request");
    }
    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    public BadRequestException(String message, Object... args) {
        super(message, args);
    }

}
