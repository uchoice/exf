package net.uchoice.exf.core.config.exception;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfException;

public class ActionConfigException extends ExfException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActionConfigException(ErrorMessage errorMessage, Throwable root) {
		super(errorMessage, root);
	}
	
	public ActionConfigException(String errorMessage, Throwable root) {
        super(errorMessage, root);
    }
	
	public ActionConfigException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
	
	public ActionConfigException(ErrorMessage errorMessage) {
		super(errorMessage);
	}
}
