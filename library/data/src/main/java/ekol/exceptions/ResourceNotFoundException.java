package ekol.exceptions;

/**
 * Created by fatmaozyildirim on 3/30/16.
 */

public class ResourceNotFoundException extends ParameterizedRuntimeException {

    public ResourceNotFoundException() {
        super("Resource Not Found");
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public ResourceNotFoundException(String message, Object... args) {
        super(message, args);
    }
}


