package net.uchoice.exf.core.runtime.invoker.handler;

import java.lang.reflect.InvocationTargetException;

import com.alibaba.fastjson.JSONArray;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfException;
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
 * ACTION方法执行处理
 */
public class MethodHandler implements ActionHandler {

	private static MethodHandler instance = new MethodHandler();

	@Override
	public boolean accept(ActionInst action, Context context) {
		if (null != action && null != action.getSpec() && null != action.getSpec().getMethod()) {
			return true;
		}
		return false;
	}

	@Override
	public ActionResult handle(ActionInst action, Context context) {
		ExfTracker.start(Record.create(RecordType.HANDLER, action.getSpec().getType().getName()));
		// 参数封装
		Object[] params = new Object[action.getSpec().getInVariables().size()];
		int index = 0;
		for (String name : action.getSpec().getInVariables().keySet()) {
			if (action.getInVariables().containsKey(name)) {
				params[index] = action.getInVariables().get(name)
						.getObject(action.getInVariables().get(name).getVariableSpec().getType());
			} else {
				params[index] = null;
			}
			ExfTracker.addInput(params[index]);
			index++;
		}
		// 方法执行
		try {
			ActionResult result = new ActionResult();
			Object obj = action.getSpec().getMethod().invoke(action, params);
			result = parseResult(obj, result);
			ExfTracker.success(result.getState().isSuccess());
			ExfTracker.stop(result);
			return result;
		} catch (Throwable e) {
			// 调用异常则直接提取异常Cause
			if (e instanceof InvocationTargetException) {
				e = e.getCause();
			}
			// 如果业务异常 要求中断则中断业务，否则标记执行错误后继续
			ErrorMessage errorMsg = null;
			if (e instanceof ExfException) {
				errorMsg = ((ExfException) e).getErrorMessage();
			}
			if (e instanceof ExfRuntimeException) {
				errorMsg = ((ExfRuntimeException) e).getErrorMessage();
			}
			if (null != errorMsg && !errorMsg.isForceThrowException()) {
				ExfTracker.success(false, errorMsg.getErrorMessage());
				ExfTracker.stop();
				return ActionResult.buildErrorResult(e);
				// 如果是未声名的业务异常则强制中断
			} else {
				ErrorMessage error = ErrorMessage.code(true, "C-EXF-02-01-005", action.getSpec().getClass().getName(),
						action.getSpec().getMethod(), JSONArray.toJSONString(params), action.getUid());
				ExfTracker.success(false, error.getErrorMessage());
				ExfTracker.stop();
				throw new ExfRuntimeException(error, e);
			}
		}
	}

	/**
	 * 处理结果，支持结果类型 ActionResult/Boolean/void/Object
	 * 
	 * @param obj
	 * @param result
	 */
	private ActionResult parseResult(Object obj, ActionResult result) {
		if (obj instanceof Boolean) {
			if ((Boolean) obj == false) {
				result.setState(ActionState.FAIL);
			}
			result.setResult(obj);
		} else if (obj instanceof ActionResult) {
			result = (ActionResult) obj;
		} else {
			result.setResult(obj);
		}
		return result;
	}

	public static MethodHandler get() {
		return instance;
	}

	public static void main(String[] args) {
		Object obj = true;
		if (obj instanceof Boolean) {
			if ((Boolean) obj == true) {
				System.out.println(true);
			}
		}
	}
}
