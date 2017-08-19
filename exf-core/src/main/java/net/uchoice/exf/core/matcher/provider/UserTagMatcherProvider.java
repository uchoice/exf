package net.uchoice.exf.core.matcher.provider;

import java.util.Map;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.core.matcher.Matcher;
import net.uchoice.exf.core.matcher.exception.MatchException;
import org.apache.commons.lang.StringUtils;

/**
 * OGNL 表达式匹配处理器
 */
public class UserTagMatcherProvider implements MatchProvider {

	@Override
	public boolean match(Matcher matcher, Map<String, Object> context) throws MatchException {
		if (null == matcher || StringUtils.isBlank(matcher.getTest())) {
			return false;
		}
		try {
			return true;
		} catch (Exception e) {
			throw new MatchException(ErrorMessage.code("C-EXF-02-01-002", matcher.getTest()), e);
		}
	}

}
