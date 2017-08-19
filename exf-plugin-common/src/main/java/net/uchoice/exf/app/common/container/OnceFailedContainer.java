package net.uchoice.exf.app.common.container;

import java.util.Map;
import java.util.function.Predicate;

import net.uchoice.exf.model.action.ActionResult;
import net.uchoice.exf.model.container.PredicateContainer;
import net.uchoice.exf.model.container.annotation.Container;

@Container(code = OnceFailedContainer.CODE, name = "一旦失败退出策略容器")
public class OnceFailedContainer extends PredicateContainer {

	public static final String CODE = "net.uchoice.exf.app.common.container.OnceFailedContainer";
	
	@Override
	protected Predicate<Map<String, ActionResult>> getBreaker() {
		return (t) -> {
			for (String key : t.keySet()) {
				if (!t.get(key).getState().isSuccess())
					return false;
			}
			return true;
		};
	}

}
