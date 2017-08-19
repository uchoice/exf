package net.uchoice.exf.core.runtime.invoker.handler;

import java.util.Map;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfRuntimeException;
import net.uchoice.exf.core.context.Context;
import net.uchoice.exf.core.runtime.invoker.ActionHandler;
import net.uchoice.exf.core.trace.ExfTracker;
import net.uchoice.exf.core.trace.Record;
import net.uchoice.exf.core.trace.RecordType;
import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.ActionResult;
import net.uchoice.exf.model.action.ActionState;

/**
 * 身份规则处理
 */
public class IdentifyHandler implements ActionHandler {

	private static IdentifyHandler instance = new IdentifyHandler();

	@Override
	public boolean accept(ActionInst action, Context context) {
		if (action.getMatcher() != null) {
			return true;
		}
		return false;
	}

	@Override
	public ActionResult handle(ActionInst action, Context context) {
		ActionResult result = null;
		try {
			Map<String, Object> contextMap = context.extractContext();
			ExfTracker.start(Record.create(RecordType.HANDLER, getClass().getName()).addInput(action.getMatcher()).addInput(contextMap));
			boolean matchResult = action.getMatcher().match(contextMap);
			if (!matchResult) {
				// 匹配成则放行
				result = new ActionResult();
				result.setState(ActionState.NO_MATCH);
			}
		} catch (Throwable e) {
			ErrorMessage error = ErrorMessage.code(true, "C-EXF-02-01-004", action.getMatchExpression(),
					action.getUid());
			ExfTracker.success(false, error.getErrorMessage());
			ExfTracker.stop();
			throw new ExfRuntimeException(error, e);
		}
		ExfTracker.stop(result);
		return result;
	}

	public static IdentifyHandler get() {
		return instance;
	}
}
