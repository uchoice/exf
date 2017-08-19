package net.uchoice.exf.core.config.exception;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfException;

public class ContainerConfigException extends ExfException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContainerConfigException(ErrorMessage errorMessage, Throwable root) {
		super(errorMessage, root);
	}
	
	public ContainerConfigException(String errorMessage, Throwable root) {
        super(errorMessage, root);
    }
	
	public ContainerConfigException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
	
	public ContainerConfigException(ErrorMessage errorMessage) {
		super(errorMessage);
	}
}
