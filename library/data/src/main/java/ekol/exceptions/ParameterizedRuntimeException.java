package ekol.exceptions;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Created by kilimci on 06/04/16.
 */
public abstract class ParameterizedRuntimeException extends RuntimeException{
    private final transient Object[] args;

    public ParameterizedRuntimeException(String message) {
        super(message);
        this.args = new Serializable[]{};
    }
    public ParameterizedRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.args = new Serializable[]{};

    }
    public ParameterizedRuntimeException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public String getMessage(){
        if(getArgs() != null && getArgs().length > 0){
            MessageFormat messageFormat = new MessageFormat(super.getMessage());
            return messageFormat.format(getArgs());
        }
        return super.getMessage();
    }

    public String getRawMessage() {
        return super.getMessage();
    }
}
