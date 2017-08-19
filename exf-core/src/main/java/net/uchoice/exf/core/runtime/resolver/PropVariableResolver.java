package net.uchoice.exf.core.runtime.resolver;

import net.uchoice.exf.core.runtime.resolver.advisor.JsonVariableAdvisor;
import net.uchoice.exf.core.runtime.resolver.advisor.SpringVariableAdvisor;

/**
 * 属性参数解析器
 */
public class PropVariableResolver extends DefaultVariableResolver {

	private static PropVariableResolver instance;
	
	public PropVariableResolver() {
		advisors.add(JsonVariableAdvisor.get());
		advisors.add(SpringVariableAdvisor.get());
	}
	
	public static PropVariableResolver get() {
		if (null == instance) {
			instance = new PropVariableResolver();
		}
		return instance;
	}
}
