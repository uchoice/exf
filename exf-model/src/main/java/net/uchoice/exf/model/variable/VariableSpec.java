package net.uchoice.exf.model.variable;

import java.lang.reflect.Type;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class VariableSpec {

	private String name;
	
	private VariableMode mode;
	
	private Class<?> type;
	
	private Type generateType;
	
	private int index;
	
	private boolean required;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VariableMode getMode() {
		return mode;
	}

	public void setMode(VariableMode mode) {
		this.mode = mode;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Type getGenerateType() {
		return generateType;
	}

	public void setGenerateType(Type generateType) {
		this.generateType = generateType;
	}
	
	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public static VariableSpec of (VariableMode mode, String name, Class<?> type, int index, boolean isRequired) {
		VariableSpec variableSpec = new VariableSpec();
		variableSpec.setIndex(index);
		variableSpec.setMode(mode);
		variableSpec.setName(name);
		variableSpec.setType(type);
		variableSpec.setRequired(isRequired);
		return variableSpec;
	}
	
	public static VariableSpec of (VariableMode mode, String name, Class<?> type, int index) {
		return of(mode, name, type, index, false);
	}
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
