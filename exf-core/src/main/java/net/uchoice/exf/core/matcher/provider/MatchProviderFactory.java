package net.uchoice.exf.core.matcher.provider;

import java.util.HashMap;
import java.util.Map;

import net.uchoice.exf.core.matcher.MatcherType;
import org.apache.commons.lang.StringUtils;

/**
 * 匹配执行器工厂
 */
public class MatchProviderFactory {

	private static Map<String, MatchProvider> providers = new HashMap<>();

	static {
		for (MatcherType matcherType : MatcherType.values()) {
			providers.put(matcherType.getType(), matcherType.getProvider());
		}
	}

	private MatchProviderFactory() {
		super();
	}

	/**
	 * 为匹配器生成匹配 执行器
	 * 
	 * @param matcher
	 * @return
	 */
	public static MatchProvider create(String type) {
		if (StringUtils.isBlank(type)) {
			return null;
		}
		return providers.get(type);
	}
}
