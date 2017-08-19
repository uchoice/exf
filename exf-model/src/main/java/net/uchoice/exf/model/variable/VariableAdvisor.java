package net.uchoice.exf.model.variable;

public interface VariableAdvisor {

	Object evaluate(Variable variable);
	
	boolean accept(Variable variable);
}
