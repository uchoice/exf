package net.uchoice.exf.sample.plugin.action;

import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.annotation.Action;

@Action(code = UserNameCheck.CODE, name = "校验用户唯一性", method = "check")
public class UserNameCheck extends ActionInst {

	public static final String CODE = "p.l.a.UserNameCheck";
	
	/**
	 * 名字只能为数字、字母
	 */
	private static final String REG_PARTTEN = "[a-z|A-z|0-9]*";

	public boolean check(String name) {
		if (null != name && name.matches(REG_PARTTEN)) {
			return true;
		} else {
			return false;
		}
	}
}
