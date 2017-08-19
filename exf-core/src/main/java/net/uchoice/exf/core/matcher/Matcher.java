package net.uchoice.exf.core.matcher;

import java.util.Map;

import net.uchoice.exf.core.matcher.provider.MatchProvider;
import net.uchoice.exf.model.matcher.MatchAble;

/**
 * 匹配器
 */
public class Matcher implements MatchAble {
	
	private Matcher(){
		super();
	}
	
	/**
	 * 标识
	 */
	private String id;
	
	/**
	 * 匹配器类型
	 */
	private MatcherType type;
	
	/**
	 * 测试值
	 */
	private String test;
	
	/**
	 * 匹配执行器
	 */
	private MatchProvider provider;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MatcherType getType() {
		return type;
	}

	public void setType(String type) {
		this.type = MatcherType.of(type);
	}
	
	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public MatchProvider getProvider() {
		return provider;
	}

	public void setProvider(MatchProvider provider) {
		this.provider = provider;
	}

	public static Matcher of(String id, String type, String test, MatchProvider provider){
		Matcher matcher = new Matcher();
		matcher.setId(id);
		matcher.setType(type);
		matcher.setTest(test);
		matcher.setProvider(provider);
		return matcher;
	}

	/* (non-Javadoc)
	 * @see net.uchoice.exf.core.matcher.MatchAble#match(java.util.Map)
	 */
	@Override
	public boolean match(Map<String, Object> context) {
		if(null != this.provider){
			return this.provider.match(this, context);
		}
		return false;
	}
}
