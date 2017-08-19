package net.uchoice.exf.core.runtime.resolver.advisor;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfRuntimeException;
import net.uchoice.exf.core.trace.ExfTracker;
import net.uchoice.exf.core.trace.Record;
import net.uchoice.exf.core.trace.RecordType;
import net.uchoice.exf.core.util.SpringApplicationContextHolder;
import net.uchoice.exf.model.variable.Variable;
import net.uchoice.exf.model.variable.VariableAdvisor;
import org.apache.commons.lang.StringUtils;

public class SpringVariableAdvisor implements VariableAdvisor {

	private static final String PARTTEN = "spring:";

	private static SpringVariableAdvisor instance;

	@Override
	public Object evaluate(Variable variable) {
		ExfTracker.start(Record.create(RecordType.VARIABLE, getClass().getName()).addInput(variable));
		try {
			Object result = SpringApplicationContextHolder.getSpringBean(variable.getValue().replace(PARTTEN, ""),
					variable.getVariableSpec().getType());
			ExfTracker.stop(result);
			return result;
		} catch (Throwable e) {
			ErrorMessage error = ErrorMessage.code("C-EXF-02-01-008", variable.getVariableSpec(), variable.getValue());
			ExfTracker.success(false, error.getErrorMessage());
			ExfTracker.stop();
			throw new ExfRuntimeException(error, e);
		}
	}

	@Override
	public boolean accept(Variable variable) {
		if (null == variable || StringUtils.isBlank(variable.getValue()) || null == variable.getVariableSpec()) {
			return false;
		}
		if (variable.getValue().startsWith(PARTTEN)) {
			return true;
		}
		return false;
	}

	public static SpringVariableAdvisor get() {
		if (null == instance) {
			instance = new SpringVariableAdvisor();
		}
		return instance;
	}
}
