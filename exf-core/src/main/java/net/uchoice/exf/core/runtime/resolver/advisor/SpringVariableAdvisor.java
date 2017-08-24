package net.uchoice.exf.core.runtime.resolver.advisor;

import java.util.Map;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfRuntimeException;
import net.uchoice.exf.core.trace.ExfTracker;
import net.uchoice.exf.core.trace.Record;
import net.uchoice.exf.core.trace.RecordType;
import net.uchoice.exf.core.util.SpringApplicationContextHolder;
import net.uchoice.exf.model.variable.Variable;
import net.uchoice.exf.model.variable.VariableAdvisor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

public class SpringVariableAdvisor implements VariableAdvisor {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpringVariableAdvisor.class);

	private static final String PARTTEN = "spring:";

	private static SpringVariableAdvisor instance;

	@Override
	public Object evaluate(Variable variable) {
		ExfTracker.start(Record.create(RecordType.VARIABLE, getClass().getName()).addInput(variable));
		try {
			Object result = null;
			try {
				result = SpringApplicationContextHolder.getSpringBean(variable.getValue().replace(PARTTEN, ""),
						variable.getVariableSpec().getType());
			} catch (BeansException e) {
				LOG.error(String.format("spring bean name[%s] not found", variable.getValue().replace(PARTTEN, "")), e);
			}
			if (null == result) {
				LOG.debug(String.format("spring bean name[%s] not found", variable.getValue().replace(PARTTEN, "")));
				// 按照类型进行查找
				Map<String, ?> beans = SpringApplicationContextHolder
						.getSpringBeans(variable.getVariableSpec().getType());
				if (null != beans && !beans.isEmpty()) {
					LOG.debug(String.format("spring bean type[%s] find [%s] instances",
							variable.getVariableSpec().getType(), beans.size()));
					String beanName = beans.keySet().iterator().next();
					result = beans.get(beanName);
					LOG.debug(String.format("spring bean name[%s] wired by type[%s], instance name[%s]",
							variable.getValue().replace(PARTTEN, ""), variable.getVariableSpec().getType(), beanName));
				}
			}
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
