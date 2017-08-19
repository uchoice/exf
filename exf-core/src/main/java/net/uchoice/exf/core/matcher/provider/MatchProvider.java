package net.uchoice.exf.core.matcher.provider;

import java.util.Map;

import net.uchoice.exf.core.matcher.Matcher;
import net.uchoice.exf.core.matcher.exception.MatchException;

/**
 * 匹配能力提供器
 *
 */
public interface MatchProvider {

	boolean match(Matcher matcher, Map<String, Object> context) throws MatchException;
	
}
