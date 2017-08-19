package net.uchoice.exf.core.matcher.logic;

import java.util.Map;

public class AndLogicMatcher extends LogicMatcher {

	@Override
	public boolean match(Map<String, Object> context) {
		if (null != right && null != left) {
			return left.match(context) && right.match(context);
		}
		return false;
	}

}
