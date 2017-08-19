package net.uchoice.exf.client.exception;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

public class ExfException extends NestableException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * the errorMessages.
     */
    private List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();

    /**
     *
     * @param errorMessage error message.
     * @param root         root throwable exception.
     */
    public ExfException(String errorMessage, Throwable root) {
        super(errorMessage, root);
    }

    public ExfException(Throwable root) {
        super(root);
        ErrorMessage errorMessage = null;
        if (root instanceof ExfException) {
            errorMessage = ((ExfException) root).getFirstErrorMessage();
        } else if (root instanceof RuntimeException) {
            errorMessage = getErrorMessage((RuntimeException) root);
        }
        if (null != errorMessage) {
            this.errorMessages.add(errorMessage);
        } else {
            this.errorMessages.add(ErrorMessage.defaultError());
        }
    }

    public ExfException(@Nonnull ErrorMessage errorMessage, Throwable root) {
        super(errorMessage.getErrorMessage(), root);
        this.errorMessages.add(errorMessage);
    }

    /**
     * @param errorMessage error message.
     */
    public ExfException(@Nonnull ErrorMessage errorMessage) {
        super(errorMessage.getErrorMessage());
        this.errorMessages.add(errorMessage);
    }

    /**
     * @param errorCode    error code.
     * @param errorMessage error message.
     */
    public ExfException(String errorCode, String errorMessage) {
        this(ErrorMessage.of(errorCode, errorMessage));
    }

    private ErrorMessage getErrorMessage(RuntimeException runtimeException) {
        if (null == runtimeException)
            return null;
        Throwable throwable = runtimeException.getCause();
        if (null == throwable)
            return null;
        if (throwable instanceof ExfException) {
            return ((ExfException) throwable).getFirstErrorMessage();
        } else if (throwable instanceof RuntimeException) {
            return getErrorMessage((RuntimeException) throwable);
        }
        return null;
    }


    /**
     * add extra error message.
     *
     * @param errorMessage error message.
     * @return current TMFException.
     */
    public ExfException addErrorMessage(@Nonnull ErrorMessage errorMessage) {
        errorMessages.add(errorMessage);
        return this;
    }

    public ErrorMessage getFirstErrorMessage() {
        if (null == errorMessages || errorMessages.isEmpty())
            return ErrorMessage.defaultError();
        return errorMessages.get(0);
    }

    public ErrorMessage getErrorMessage() {
        return getFirstErrorMessage();
    }

    @Override
    public String toString() {
        StringBuffer errorInfo = new StringBuffer();
        for (ErrorMessage err : errorMessages) {
            errorInfo.append(err.toString()).append("\r\n");
        }
        if (null != this.getCause()) {
            errorInfo.append(this.getCause().getMessage());
        }
        return errorInfo.toString();
    }

    @Override
    public String getMessage() {
        ErrorMessage errorMessage = this.getErrorMessage();
        if (null != errorMessage) {
            String error = errorMessage.getErrorMessage();
            if (null != error)
                return error;
        }
        String error = super.getMessage();
        if (StringUtils.isNotEmpty(error)) {
            return error;
        }
        return ErrorMessage.defaultError().toString();
    }

	public List<ErrorMessage> getErrorMessages() {
		return errorMessages;
	}
}
