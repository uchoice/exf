package net.uchoice.exf.core.runtime.invoker.handler;

import java.util.Map;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfRuntimeException;
import net.uchoice.exf.core.context.Context;
import net.uchoice.exf.core.expression.groovy.GroovyExpression;
import net.uchoice.exf.core.runtime.invoker.ActionHandler;
import net.uchoice.exf.core.trace.ExfTracker;
import net.uchoice.exf.core.trace.Record;
import net.uchoice.exf.core.trace.RecordType;
import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.ActionResult;
import net.uchoice.exf.model.action.ScriptAction;
import net.uchoice.exf.model.action.ScriptType;

/**
 * 表达式处理
 */
public class GroovyScriptHandler implements ActionHandler {

	private static GroovyScriptHandler instance = new GroovyScriptHandler();

	@Override
	public boolean accept(ActionInst action, Context context) {
		if (action instanceof ScriptAction && ((ScriptAction) action).getType() == ScriptType.GROOVY) {
			return true;
		}
		return false;
	}

	@Override
	public ActionResult handle(ActionInst action, Context context) {
		ActionResult actionResult = new ActionResult();
		String expression = ((ScriptAction) action).getExpression();
		try {
			Map<String, Object> contextMap = context.extractContext();
			ExfTracker.start(Record.create(RecordType.HANDLER, getClass().getName()).addInput(expression).addInput(contextMap));
			GroovyExpression groovyExpression = new GroovyExpression(contextMap);
			Map<String, Object> result = groovyExpression.evaluate(expression, ((ScriptAction) action).getScriptName());
			actionResult.getModel().putAll(result);
		} catch (Throwable e) {
			// script action error must be thrown
			ErrorMessage error = ErrorMessage.code(true, "C-EXF-02-01-003", expression, action.getUid());
			ExfTracker.success(false, error.getErrorMessage());
			ExfTracker.stop();
			throw new ExfRuntimeException(error, e);
		}
		ExfTracker.stop(actionResult);
		return actionResult;
	}

	public static GroovyScriptHandler get() {
		return instance;
	}
}
