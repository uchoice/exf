package net.uchoice.exf.model.variable;

public interface VariableResolver {

	void addAdvisor(VariableAdvisor advisor);
	
	Object resolve(Variable variable);
}
