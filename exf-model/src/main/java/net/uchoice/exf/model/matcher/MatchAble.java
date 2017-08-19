package net.uchoice.exf.model.matcher;

import java.util.Map;

public interface MatchAble {

	/**
	 * 匹配
	 * @param context 上下文
	 * @return boolean
	 */
	boolean match(Map<String, Object> context);
	
}