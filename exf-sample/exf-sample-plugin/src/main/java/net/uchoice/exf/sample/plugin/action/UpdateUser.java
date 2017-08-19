package net.uchoice.exf.sample.plugin.action;

import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.annotation.Action;
import net.uchoice.exf.model.variable.annotation.Prop;
import net.uchoice.exf.sample.plugin.dao.UserDao;
import net.uchoice.exf.sample.plugin.model.User;

@Action(code = UpdateUser.CODE, name = "更新用户信息", method = "update")
public class UpdateUser extends ActionInst {

	public static final String CODE = "p.l.a.UpdateUser";
	
	@Prop(required = true)
	private UserDao userDao;
	
	public void update(User user){
		userDao.addUser(user);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
