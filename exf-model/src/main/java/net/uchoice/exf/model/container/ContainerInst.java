package net.uchoice.exf.model.container;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.ActionResult;
import net.uchoice.exf.model.common.BaseInstance;

public abstract class ContainerInst extends BaseInstance {
	
	protected ContainerSpec spec;

	protected List<ActionInst> actions = new ArrayList<ActionInst>();

	protected ContainerInst next;

	protected Map<String, ActionResult> executionResults = new LinkedHashMap<String, ActionResult>();

	public void invoke() {
		if (!actions.isEmpty() && execute(actions) && null != next) {
			next.invoke();
		}
	}

	public abstract boolean execute(List<ActionInst> actions);

	public List<ActionInst> getActions() {
		return actions;
	}

	public void setActions(List<ActionInst> actions) {
		this.actions = actions;
	}

	public void addAction(ActionInst action) {
		if (null != action) {
			//TODO 此处需要作action的必填属性校验,ScriptAction 检查expression
			this.actions.add(action);
		}
	}

	public ContainerInst getNext() {
		return next;
	}

	public void setNext(ContainerInst next) {
		this.next = next;
	}

	public ContainerSpec getSpec() {
		return spec;
	}

	public void setSpec(ContainerSpec spec) {
		this.spec = spec;
	}
}
