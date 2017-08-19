package net.uchoice.exf.app.common.container;

import java.util.Map;
import java.util.function.Predicate;

import net.uchoice.exf.model.action.ActionResult;
import net.uchoice.exf.model.container.PredicateContainer;
import net.uchoice.exf.model.container.annotation.Container;

@Container(code = AllExecuteContainer.CODE, name = "所有节点执行容器")
public class AllExecuteContainer extends PredicateContainer {

	public static final String CODE = "net.uchoice.exf.app.common.container.AllExecuteContainer";
	
	@Override
	protected Predicate<Map<String, ActionResult>> getBreaker() {
		return null;
	}

}
