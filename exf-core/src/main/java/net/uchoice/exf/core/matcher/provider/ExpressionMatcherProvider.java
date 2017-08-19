package net.uchoice.exf.core.matcher.provider;

import java.util.Map;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.core.expression.ognl.OgnlExpression;
import net.uchoice.exf.core.matcher.Matcher;
import net.uchoice.exf.core.matcher.exception.MatchException;
import net.uchoice.exf.core.trace.ExfTracker;
import net.uchoice.exf.core.trace.Record;
import net.uchoice.exf.core.trace.RecordType;
import org.apache.commons.lang.StringUtils;

/**
 * OGNL 表达式匹配处理器
 */
public class ExpressionMatcherProvider implements MatchProvider {

	@Override
	public boolean match(Matcher matcher, Map<String, Object> context) throws MatchException {
		ExfTracker.start(Record.create(RecordType.MATCH, getClass().getName()).addInput(matcher).addInput(context));
		if (null == matcher || StringUtils.isBlank(matcher.getTest())) {
			ExfTracker.success(false);
			ExfTracker.stop();
			return false;
		}
		OgnlExpression expression = new OgnlExpression(context);
		try {
			boolean result = expression.evaluate(matcher.getTest(), Boolean.class);
			ExfTracker.stop(result);
			return result;
		} catch (Throwable e) {
			ErrorMessage error = ErrorMessage.code("C-EXF-02-01-001", matcher.getTest());
			ExfTracker.success(false, error.getErrorMessage());
			ExfTracker.stop();
			throw new MatchException(error, e);
		}
	}

}
