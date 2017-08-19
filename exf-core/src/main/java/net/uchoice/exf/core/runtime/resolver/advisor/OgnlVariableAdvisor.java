package net.uchoice.exf.core.runtime.resolver.advisor;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfRuntimeException;
import net.uchoice.exf.core.expression.ognl.OgnlExpression;
import net.uchoice.exf.core.runtime.Session;
import net.uchoice.exf.core.trace.ExfTracker;
import net.uchoice.exf.core.trace.Record;
import net.uchoice.exf.core.trace.RecordType;
import net.uchoice.exf.model.variable.Variable;
import net.uchoice.exf.model.variable.VariableAdvisor;
import ognl.OgnlException;
import org.apache.commons.lang.StringUtils;

public class OgnlVariableAdvisor implements VariableAdvisor {

	private static final String PARTTEN = "#";

	private static OgnlVariableAdvisor instance;

	@Override
	public Object evaluate(Variable variable) {
		ExfTracker.start(Record.create(RecordType.VARIABLE, getClass().getName()).addInput(variable));
		try {
			Object result = new OgnlExpression(Session.getSessionContext().extractContext())
					.evaluate(variable.getValue(), variable.getVariableSpec().getType());
			ExfTracker.stop(result);
			return result;
		} catch (OgnlException e) {
			ErrorMessage error = ErrorMessage.code("C-EXF-02-01-007", variable.getVariableSpec(), variable.getValue());
			ExfTracker.success(false, error.getErrorMessage());
			ExfTracker.stop();
			throw new ExfRuntimeException(error, e);
		}
	}

	@Override
	public boolean accept(Variable variable) {
		if (null == variable || StringUtils.isBlank(variable.getValue())) {
			return false;
		}
		if (variable.getValue().startsWith(PARTTEN)) {
			return true;
		}
		return false;
	}

	public static OgnlVariableAdvisor get() {
		if (null == instance) {
			instance = new OgnlVariableAdvisor();
		}
		return instance;
	}
}