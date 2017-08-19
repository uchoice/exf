package net.uchoice.exf.core.config.exception;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfException;

public class ServiceConfigException extends ExfException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceConfigException(ErrorMessage errorMessage, Throwable root) {
		super(errorMessage, root);
	}
	
	public ServiceConfigException(String errorMessage, Throwable root) {
        super(errorMessage, root);
    }
	
	public ServiceConfigException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
	
	public ServiceConfigException(ErrorMessage errorMessage) {
		super(errorMessage);
	}
}
