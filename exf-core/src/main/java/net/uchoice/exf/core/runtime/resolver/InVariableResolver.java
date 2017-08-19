package net.uchoice.exf.core.runtime.resolver;

import net.uchoice.exf.core.runtime.resolver.advisor.JsonVariableAdvisor;
import net.uchoice.exf.core.runtime.resolver.advisor.OgnlVariableAdvisor;

/**
 * 输入参数解析器
 */
public class InVariableResolver extends DefaultVariableResolver {

	private static InVariableResolver instance;
	
	public InVariableResolver() {
		advisors.add(JsonVariableAdvisor.get());
		advisors.add(OgnlVariableAdvisor.get());
	}
	
	public static InVariableResolver get() {
		if (null == instance) {
			instance = new InVariableResolver();
		}
		return instance;
	}
}
