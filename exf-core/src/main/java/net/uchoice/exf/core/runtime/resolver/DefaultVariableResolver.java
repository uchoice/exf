package net.uchoice.exf.core.runtime.resolver;

import java.util.ArrayList;
import java.util.List;

import net.uchoice.exf.model.variable.Variable;
import net.uchoice.exf.model.variable.VariableAdvisor;
import net.uchoice.exf.model.variable.VariableResolver;

public class DefaultVariableResolver implements VariableResolver {

	protected List<VariableAdvisor> advisors = new ArrayList<>();

	@Override
	public void addAdvisor(VariableAdvisor advisor) {
		if (null != advisor) {
			advisors.add(advisor);
		}
	}

	@Override
	public Object resolve(Variable variable) {
		if (null == variable) {
			return null;
		}
		for (VariableAdvisor advisor : advisors) {
			if (advisor.accept(variable)) {
				return advisor.evaluate(variable);
			}
		}
		return null;
	}

}
