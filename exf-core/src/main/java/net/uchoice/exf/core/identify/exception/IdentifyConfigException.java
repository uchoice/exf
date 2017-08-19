package net.uchoice.exf.core.identify.exception;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfException;

public class IdentifyConfigException extends ExfException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IdentifyConfigException(ErrorMessage errorMessage, Throwable root) {
		super(errorMessage, root);
	}
	
	public IdentifyConfigException(String errorMessage, Throwable root) {
        super(errorMessage, root);
    }
	
	public IdentifyConfigException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
	
	public IdentifyConfigException(ErrorMessage errorMessage) {
		super(errorMessage);
	}
}
