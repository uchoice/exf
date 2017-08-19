package net.uchoice.exf.model.container;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.ActionResult;

public abstract class PredicateContainer extends ContainerInst {

	@Override
	public boolean execute(List<ActionInst> actions) {
		for (ActionInst action : actions) {
			if (null != getBreaker() && !getBreaker().test(executionResults)) {
				return false;
			}
			ActionResult result = action.invoke();
			executionResults.put(action.getUid(), result);
		}
		return true;
	}

	protected abstract Predicate<Map<String, ActionResult>> getBreaker();

}
