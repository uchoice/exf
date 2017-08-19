package net.uchoice.exf.sample.plugin.action;

import java.util.List;

import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.ActionResult;
import net.uchoice.exf.model.action.ActionState;
import net.uchoice.exf.model.action.annotation.Action;
import net.uchoice.exf.model.variable.annotation.Prop;

@Action(code = UserAgeCheck.CODE, name = "用户年龄段判断", method = "check")
public class UserAgeCheck extends ActionInst {

	public static final String CODE = "p.l.a.UserAgeCheck";

	@Prop(required = true)
	private List<Integer> range;

	public ActionResult check(Integer age) {
		if (null != age && null != range && range.contains(age)) {
			return new ActionResult();
		} else {
			return new ActionResult(ActionState.FAIL);
		}
	}
}
