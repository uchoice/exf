package net.uchoice.exf.model.variable;

public enum VariableMode {

	IN, PROP;

	public static VariableMode of(String mode){
		if(VariableMode.IN.name().equalsIgnoreCase(mode)){
			return VariableMode.IN;
		}
		if(VariableMode.PROP.name().equalsIgnoreCase(mode)){
			return VariableMode.PROP;
		}
		return null;
	}
}
