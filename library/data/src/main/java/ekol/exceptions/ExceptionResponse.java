package ekol.exceptions;

/**
 * Created by ozer on 21/08/2017.
 */
public class ExceptionResponse {

    private String message;
    private Object[] args;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String message, Object... args) {
        this.message = message;
        this.args = args;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
