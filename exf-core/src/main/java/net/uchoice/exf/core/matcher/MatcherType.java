package net.uchoice.exf.core.matcher;

import net.uchoice.exf.core.matcher.provider.ExpressionMatcherProvider;
import net.uchoice.exf.core.matcher.provider.MatchProvider;
import net.uchoice.exf.core.matcher.provider.UserTagMatcherProvider;

public enum MatcherType {

	/**
	 * 表达式匹配：支持OGNL表达式规则匹配
	 */
	EXPRESSION("expression", new ExpressionMatcherProvider()),
	
	/**
	 * UIC用户标签匹配 
	 */
	USER_TAG("userTag", new UserTagMatcherProvider());
	
	private String type;
	
	private MatchProvider provider;
	
	private MatcherType(String type, MatchProvider provider){
		this.type = type;
		this.provider = provider;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MatchProvider getProvider() {
		return provider;
	}

	public void setProvider(MatchProvider provider) {
		this.provider = provider;
	}

	public static MatcherType of(String type) {
		for (MatcherType matcher : MatcherType.values()) {
			if (matcher.getType().equalsIgnoreCase(type)) {
				return matcher;
			}
		}
		return null;
	}
}
