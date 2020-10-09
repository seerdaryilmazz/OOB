package ekol.crm.quote.queue.exportq.exception;

import ekol.exceptions.ParameterizedRuntimeException;

public class ExportFailureException extends ParameterizedRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2290933339284994459L;
	
	public ExportFailureException() {
        super("Export Consecutive Failure");
    }
    public ExportFailureException(String message) {
        super(message);
    }
    public ExportFailureException(String message, Throwable cause) {
        super(message, cause);
    }
    public ExportFailureException(String message, Object... args) {
        super(message, args);
    }

}
