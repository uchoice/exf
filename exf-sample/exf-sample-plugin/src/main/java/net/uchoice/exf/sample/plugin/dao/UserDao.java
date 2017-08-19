package net.uchoice.exf.sample.plugin.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.uchoice.exf.sample.plugin.model.User;

public class UserDao {

	private static Map<String, User> users = new ConcurrentHashMap<String, User>(8);

	static {
		users.put("1", new User("1", "ZhangSan", 5));
		users.put("2", new User("2", "LiSi", 10));
		users.put("3", new User("3", "WangWu", 15));
		users.put("4", new User("4", "JiaLiu", 20));
		users.put("5", new User("5", "QianQi", 30));
	}

	public void addUser(User user) {
		users.put(user.getId(), user);
	}

	public User getUser(String id) {
		return users.get(id);
	}
	
	
}
