package net.uchoice.exf.sample.plugin.action;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfException;
import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.ActionResult;
import net.uchoice.exf.model.action.annotation.Action;
import net.uchoice.exf.model.variable.annotation.Prop;
import net.uchoice.exf.sample.plugin.dao.UserDao;
import net.uchoice.exf.sample.plugin.model.User;

@Action(code = UserExistCheck.CODE, name = "校验用户唯一性", method = "check")
public class UserExistCheck extends ActionInst {

	public static final String CODE = "p.l.a.UserExistCheck";
	
	@Prop(required = true)
	private UserDao userDao;

	public ActionResult check(String id) throws ExfException {
		ActionResult result = null;
		User user = userDao.getUser(id);
		if (null != user) {
			result = new ActionResult();
			result.put("user", user);
			return result;
		} else {
			throw new ExfException(ErrorMessage.code("B-SAMPLE-01-01-001", id));
		}
	}
}
