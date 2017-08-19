package net.uchoice.exf.client.exception;

import org.apache.commons.lang3.StringUtils;

public class ExfRuntimeException extends NestableRuntimeException {

	private static final long serialVersionUID = 1L;

    /**
     * the real error message.
     */
    private ErrorMessage realErrorMessage = null;

    /**
     * TMFRuntimeException.
     *
     * @param th throwable exception.
     */
    public ExfRuntimeException(Throwable th) {
        super(th);
        if (th instanceof ExfException) {
            realErrorMessage = ((ExfException) th).getFirstErrorMessage();
        } else if (th instanceof ExfRuntimeException) {
            realErrorMessage = ((ExfRuntimeException) th).getErrorMessage();
        }
        realErrorMessage = ErrorMessage.defaultError();
    }
    
    public ExfRuntimeException(ErrorMessage errorMessage, Throwable th) {
    	super(th);
        this.realErrorMessage = errorMessage;
    }

    public ExfRuntimeException(ErrorMessage errorMessage) {
        this.realErrorMessage = errorMessage;
    }
    
    public ExfRuntimeException(String errorCode, String errorMessage) {
        this.realErrorMessage = ErrorMessage.of(errorCode, errorMessage);
    }

    public ExfRuntimeException(Throwable th, ErrorMessage errorMessage) {
        super(th);
        this.realErrorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        if (null != realErrorMessage)
            return realErrorMessage;
        return ErrorMessage.defaultError();
    }

    @Override
    public String getMessage() {
        ErrorMessage errorMessage = this.getErrorMessage();
        if (null != errorMessage) {
            String error = errorMessage.toString();
            if (null != error)
                return error;
        }
        String error = super.getMessage();
        if (StringUtils.isNotEmpty(error)) {
            return error;
        }
        return ErrorMessage.defaultError().toString();
    }
}
