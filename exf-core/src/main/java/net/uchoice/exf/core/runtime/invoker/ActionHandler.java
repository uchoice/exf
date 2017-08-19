package net.uchoice.exf.core.runtime.invoker;

import net.uchoice.exf.core.context.Context;
import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.ActionResult;

public interface ActionHandler {

	boolean accept(ActionInst action, Context context);

	/**
	 * handler处理
	 * 
	 * @param action 
	 * @param context 上下文
	 * @return ActionResult可空，若当前处理需要立即返回时设置结果，无需返回则返回null
	 */
	ActionResult handle(ActionInst action, Context context);
}
