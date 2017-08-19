package net.uchoice.exf.model.action;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfException;
import net.uchoice.exf.model.common.BaseInstance;
import net.uchoice.exf.model.matcher.MatchAble;
import net.uchoice.exf.model.variable.Variable;
import net.uchoice.exf.model.variable.VariableMode;
import net.uchoice.exf.model.variable.VariableSpec;

public abstract class ActionInst extends BaseInstance {

	private Function<ActionInst, ActionResult> invoker;
	
	protected String matchExpression;
	
	protected MatchAble matcher;

	protected ActionSpec spec;

	/**
	 * 有可能因为配置的先后出现乱序，运行时需要重排序
 	 */
	private Map<String, Variable> inVariables = new LinkedHashMap<String, Variable>();

	public ActionResult invoke() {
		if (null == invoker) {
			return new ActionResult(ActionState.FAIL);
		}
		return invoker.apply(this);
	}

	public ActionSpec getSpec() {
		return spec;
	}

	public void setSpec(ActionSpec spec) {
		this.spec = spec;
	}

	public Function<ActionInst, ActionResult> getInvoker() {
		return invoker;
	}

	public void setInvoker(Function<ActionInst, ActionResult> invoker) {
		this.invoker = invoker;
	}

	public Map<String, Variable> getInVariables() {
		return inVariables;
	}

	public void setInVariables(Map<String, Variable> inVariables) {
		this.inVariables = inVariables;
	}

	public MatchAble getMatcher() {
		return matcher;
	}

	public void setMatcher(MatchAble matcher) {
		this.matcher = matcher;
	}

	public String getMatchExpression() {
		return matchExpression;
	}

	public void setMatchExpression(String matchExpression) {
		this.matchExpression = matchExpression;
	}

	public void addVariable(Variable variable)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, ExfException {
		if (null == variable || null == variable.getVariableSpec()) {
			return;
		}
		// 参数变量不需要立即初始化，需要加入参数列表, 在运行时初始化
		if (variable.getVariableSpec().getMode() == VariableMode.IN) {
			VariableSpec variableSpec = this.getSpec().getInVariables().get(variable.getVariableSpec().getName());
			if (null == variableSpec) {
				throw new ExfException(ErrorMessage.code("S-EXF-01-01-011", variable.getVariableSpec().getName()));
			}
			variable.setVariableSpec(variableSpec);
			this.inVariables.put(variable.getVariableSpec().getName(), variable);
		}
		// 属性变量需要立即初始化
		if (variable.getVariableSpec().getMode() == VariableMode.PROP) {
			VariableSpec variableSpec = this.getSpec().getPropVariables().get(variable.getVariableSpec().getName());
			if (null == variableSpec) {
				throw new ExfException(ErrorMessage.code("S-EXF-01-01-012", variable.getVariableSpec().getName()));
			}
			variable.setVariableSpec(variableSpec);
			// 对当前属性进行赋值
			Object value = variable.getObject(variableSpec.getType());
			if(variableSpec.isRequired() && null == value){
				throw new ExfException(ErrorMessage.code("S-EXF-01-01-013", variable.getVariableSpec().getName()));
			}
			Field field = this.getClass().getDeclaredField(variableSpec.getName());
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			field.set(this, value);
		}
	}
}
