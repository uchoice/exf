package net.uchoice.exf.core.runtime.invoker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.uchoice.exf.core.context.Context;
import net.uchoice.exf.core.runtime.Session;
import net.uchoice.exf.core.runtime.invoker.handler.GroovyScriptHandler;
import net.uchoice.exf.core.runtime.invoker.handler.IdentifyHandler;
import net.uchoice.exf.core.runtime.invoker.handler.MethodHandler;
import net.uchoice.exf.core.trace.ExfTracker;
import net.uchoice.exf.core.trace.Record;
import net.uchoice.exf.core.trace.RecordType;
import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.ActionResult;
import net.uchoice.exf.model.action.ActionState;

/**
 * 运行时Action执行调度
 */
public class ActionInvoker implements Function<ActionInst, ActionResult> {

	private List<ActionHandler> handlers = new ArrayList<ActionHandler>();

	@Override
	public ActionResult apply(ActionInst action) {
		ExfTracker.start(Record.create(RecordType.ACTION, action.getName()).addInput(action));
		Context context = Session.getSessionContext();
		ActionResult result = null;
		try {
			if (handlers.isEmpty()) {
				registDefault();
			}
			for (ActionHandler handler : handlers) {
				if (handler.accept(action, context)) {
					result = handler.handle(action, context);
					// 当有一个处理器处理完结果后返回
					if (null != result) {
						break;
					}
				}
			}
			// 填充上下文,将结果模型中数据补齐至上下文
			context.fillContext(action.getUid(), result);
			// 如果没有任何处理器能处理，则认为处理失败
			if (null == result) {
				result = new ActionResult(ActionState.FAIL);
			}
			// 记录当前执行结果
			context.setContextResult(result);
		} finally {
			if (null != result) {
				ExfTracker.success(result.getState().isSuccess(), result.getErrMessage());
			} else {
				ExfTracker.success(false);
			}
			ExfTracker.stop(result);
		}
		return result;
	}

	public List<ActionHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<ActionHandler> handlers) {
		this.handlers = handlers;
	}

	public void registHandler(ActionHandler handler) {
		if (null != handler) {
			this.handlers.add(handler);
		}
	}

	/**
	 * 注册默认处理机制
	 */
	private void registDefault() {
		registHandler(IdentifyHandler.get());
		registHandler(GroovyScriptHandler.get());
		registHandler(MethodHandler.get());
	}
}
