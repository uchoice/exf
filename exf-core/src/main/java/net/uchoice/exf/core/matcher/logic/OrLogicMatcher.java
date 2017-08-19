package net.uchoice.exf.core.matcher.logic;

import java.util.Map;

public class OrLogicMatcher extends LogicMatcher {

	@Override
	public boolean match(Map<String, Object> context) {
		if(null != left && left.match(context)){
			return true;
		}
		if(null != right && right.match(context)){
			return true;
		}
		return false;
	}

}
