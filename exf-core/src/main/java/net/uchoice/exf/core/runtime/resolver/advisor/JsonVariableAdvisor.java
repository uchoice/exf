package net.uchoice.exf.core.runtime.resolver.advisor;

import com.alibaba.fastjson.JSON;

import com.google.gson.Gson;
import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfRuntimeException;
import net.uchoice.exf.core.trace.ExfTracker;
import net.uchoice.exf.core.trace.Record;
import net.uchoice.exf.core.trace.RecordType;
import net.uchoice.exf.model.variable.Variable;
import net.uchoice.exf.model.variable.VariableAdvisor;
import org.apache.commons.lang.StringUtils;

public class JsonVariableAdvisor implements VariableAdvisor {

	private static JsonVariableAdvisor instance;

	@Override
	public Object evaluate(Variable variable) {
		ExfTracker.start(Record.create(RecordType.VARIABLE, getClass().getName()).addInput(variable));
		try {
			Object result = new Gson().fromJson(variable.getValue(), variable.getVariableSpec().getGenerateType());
			ExfTracker.stop(result);
			return result;
		} catch (Throwable e) {
			ErrorMessage error = ErrorMessage.code("C-EXF-02-01-006", variable.getVariableSpec(), variable.getValue());
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
		try {
			JSON.parse(variable.getValue());
		} catch (Throwable e) {
			return false;
		}
		return true;
	}

	public static JsonVariableAdvisor get() {
		if (null == instance) {
			instance = new JsonVariableAdvisor();
		}
		return instance;
	}
}
