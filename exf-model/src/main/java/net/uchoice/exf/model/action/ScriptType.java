package net.uchoice.exf.model.action;

public enum ScriptType {

	GROOVY;

	public static ScriptType of(String type) {
		if (GROOVY.name().equalsIgnoreCase(type)) {
			return GROOVY;
		}
		return null;
	}
}
