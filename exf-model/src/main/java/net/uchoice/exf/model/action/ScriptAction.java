package net.uchoice.exf.model.action;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * 不允许再扩展脚本ACTION 脚本ACTION类型由系统单独支持
 *
 */
public final class ScriptAction extends ActionInst {
	
	// 脚本实例唯一标识
	private String scriptName;

	private Consumer<ScriptAction> initiator;

	private ScriptType type;

	private String expression;

	public ScriptAction() {
		this(UUID.randomUUID().toString(), null);
	}

	public ScriptAction(String scriptName, Consumer<ScriptAction> initiator) {
		this.scriptName = scriptName;
		this.initiator = initiator;
	}

	public ScriptType getType() {
		return type;
	}

	public void setType(ScriptType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
		if(null != initiator){
			initiator.accept(this);
		}
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
}
