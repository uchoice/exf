package net.uchoice.exf.model.variable;

public class Variable {

	private VariableSpec variableSpec;

	private String value;

	private Object object;

	private VariableResolver resolver;

	public VariableSpec getVariableSpec() {
		return variableSpec;
	}

	public void setVariableSpec(VariableSpec variableSpec) {
		this.variableSpec = variableSpec;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public static Variable of(VariableSpec variableSpec, String value) {
		Variable variable = new Variable();
		variable.setVariableSpec(variableSpec);
		variable.setValue(value);
		return variable;
	}

	public VariableResolver getResolver() {
		return resolver;
	}

	public void setResolver(VariableResolver resolver) {
		this.resolver = resolver;
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> cls) {
		// bugfix:由于节点实例是单例的，但不同的请求的参数传递值会变化，所以对于参数变量每次都需要重新解析
		if ((null == this.object || variableSpec.getMode() == VariableMode.IN) && null != this.resolver) {
			this.object = resolver.resolve(this);
		}
		return (T)this.object;
	}
}
