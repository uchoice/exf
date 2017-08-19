package net.uchoice.exf.core.identify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.uchoice.exf.core.matcher.Matcher;
import net.uchoice.exf.model.matcher.MatchAble;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Identify {

	/**
	 * id
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 识别器
	 */
	private MatchAble matcher;

	/**
	 * 识别表达式
	 */
	private String matchExpression;

	/**
	 * 有效匹配器
	 */
	private List<Matcher> availableMatchers = new ArrayList<>();

	/**
	 * 优先级
	 */
	private int priority;

	/**
	 * 业务扩展配置点
	 */
	private Map<String, IdentifyServiceConfig> configs = new HashMap<>();

	public Map<String, IdentifyServiceConfig> getConfigs() {
		return configs;
	}

	public void setConfigs(Map<String, IdentifyServiceConfig> configs) {
		this.configs = configs;
	}

	public Identify addConfig(IdentifyServiceConfig config) {
		if (null != config && StringUtils.isNotBlank(config.getServiceKey())) {
			this.configs.put(config.getServiceKey(), config);
		}
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MatchAble getMatcher() {
		return matcher;
	}

	public void setMatcher(MatchAble matcher) {
		this.matcher = matcher;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getMatchExpression() {
		return matchExpression;
	}

	public void setMatchExpression(String matchExpression) {
		this.matchExpression = matchExpression;
	}

	public List<Matcher> getAvailableMatchers() {
		return availableMatchers;
	}

	public void setAvailableMatchers(List<Matcher> availableMatchers) {
		this.availableMatchers = availableMatchers;
	}

	public void addAvailableMatchers(Matcher matcher) {
		if (null != matcher && StringUtils.isNotBlank(matcher.getId())) {
			this.availableMatchers.add(matcher);
		}
	}

	public static Identify of(String id, String name, String match, int priority) {
		Identify identify = new Identify();
		identify.setId(id);
		identify.setName(name);
		identify.setPriority(priority);
		identify.setMatchExpression(match);
		return identify;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
