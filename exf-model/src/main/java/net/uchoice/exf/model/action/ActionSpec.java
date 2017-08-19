package net.uchoice.exf.model.action;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import net.uchoice.exf.model.common.BaseSpec;
import net.uchoice.exf.model.variable.VariableMode;
import net.uchoice.exf.model.variable.VariableSpec;

public class ActionSpec extends BaseSpec {
	
	private Class<? extends ActionInst> type;
	
	private Method method;
	
	private Map<String,VariableSpec> inVariables = new LinkedHashMap<String,VariableSpec>();
	
	private Map<String,VariableSpec> propVariables = new LinkedHashMap<String,VariableSpec>();
	
	public Map<String, VariableSpec> getInVariables() {
		return inVariables;
	}

	public void setInVariables(Map<String, VariableSpec> inVariables) {
		this.inVariables = inVariables;
	}

	public Map<String, VariableSpec> getPropVariables() {
		return propVariables;
	}

	public void setPropVariables(Map<String, VariableSpec> propVariables) {
		this.propVariables = propVariables;
	}

	public Class<? extends ActionInst> getType() {
		return type;
	}

	public void setType(Class<? extends ActionInst> type) {
		this.type = type;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
	public void addVariable(VariableSpec variableSpec) {
		if (null != variableSpec && null != variableSpec.getMode()) {
			if (VariableMode.IN == variableSpec.getMode()) {
				inVariables.put(variableSpec.getName(), variableSpec);
			}
			if (VariableMode.PROP == variableSpec.getMode()) {
				propVariables.put(variableSpec.getName(), variableSpec);
			}
		}
	}
	
	public static ActionSpec of(String plugin, String code, String name, Class<? extends ActionInst> type, Method method){
		ActionSpec actionSpec = new ActionSpec();
		actionSpec.setPlugin(plugin);
		actionSpec.setCode(code);
		actionSpec.setMethod(method);
		actionSpec.setName(name);
		actionSpec.setType(type);
		return actionSpec;
	}
}
