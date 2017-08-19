package net.uchoice.exf.core.config.exception;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfException;

public class VariableConfigException extends ExfException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VariableConfigException(ErrorMessage errorMessage, Throwable root) {
		super(errorMessage, root);
	}
	
	public VariableConfigException(String errorMessage, Throwable root) {
        super(errorMessage, root);
    }
	public VariableConfigException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
	
	public VariableConfigException(ErrorMessage errorMessage) {
		super(errorMessage);
	}
}
