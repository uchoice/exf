package net.uchoice.exf.core.matcher.exception;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfRuntimeException;

public class MatchException extends ExfRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public MatchException(Throwable throwable) {
		super(throwable);
	}

	public MatchException(ErrorMessage errorMessage) {
		super(errorMessage);
	}
	
	public MatchException(ErrorMessage errorMessage, Throwable throwable) {
		super(errorMessage, throwable);
	}
}
