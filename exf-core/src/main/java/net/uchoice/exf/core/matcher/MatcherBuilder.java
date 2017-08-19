package net.uchoice.exf.core.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.core.matcher.exception.MatchException;
import net.uchoice.exf.core.matcher.logic.AndLogicMatcher;
import net.uchoice.exf.core.matcher.logic.LogicMatcher;
import net.uchoice.exf.core.matcher.logic.OrLogicMatcher;
import net.uchoice.exf.core.matcher.provider.ExpressionMatcherProvider;
import net.uchoice.exf.model.matcher.MatchAble;
import org.apache.commons.lang.StringUtils;

public class MatcherBuilder {

	/**
	 * 分隔符(空格,可以连续多个空格)
	 */
	private static final String TOKEN = "\\s+";

	private static final String BRACKET_LEFT = "(";

	private static final String BRACKET_RIGHT = ")";

	private static final String BRACKET_LEFT_PATTEN = "\\(";

	private static final String BRACKET_RIGHT_PATTEN = "\\)";

	private static final String BRACKET_LEFT_WITH_BLANK = " ( ";

	private static final String BRACKET_RIGHT_WITH_BLANK = " ) ";

	private static final String LOGIC_AND = "AND";

	private static final String LOGIC_OR = "OR";

	/**
	 * 将逻辑表达式构建为匹配器
	 * 
	 * @param rawMatchers
	 *            原生配置的matcher
	 * @param expression
	 *            逻辑表达式(支持 AND OR 逻辑运算符)
	 * @return
	 * @throws MatchException
	 */
	public static MatchAble build(List<Matcher> rawMatchers, String expression) throws MatchException {
		MatchAble matcher = null;
		String[] tokens = null;
		Map<String, MatchAble> valuableMatchers = new HashMap<>();
		if (StringUtils.isNotBlank(expression)) {
			expression = expression.trim();
			// 将整个表达式放到一个根内
			if (!expression.startsWith(BRACKET_LEFT) || !expression.endsWith(BRACKET_RIGHT)) {
				expression = new StringBuilder().append(BRACKET_LEFT).append(expression).append(BRACKET_RIGHT)
						.toString();
			}
			expression = expression.replaceAll(BRACKET_LEFT_PATTEN, BRACKET_LEFT_WITH_BLANK)
					.replaceAll(BRACKET_RIGHT_PATTEN, BRACKET_RIGHT_WITH_BLANK);
			tokens = expression.trim().split(TOKEN);
		}
		if (null == tokens || tokens.length == 0) {
			return matcher;
		}
		extractRawMatchers(rawMatchers, valuableMatchers);
		Stack<String> stack = new Stack<>();
		for (int index = 0; index < tokens.length; index++) {
			if (StringUtils.isBlank(tokens[index])) {
				continue;
			}
			if (isCloseParenthesis(tokens[index])) {
				Stack<String> statement = new Stack<>();
				String str = stack.pop();
				while (StringUtils.isNotBlank(str)) {
					if (isOpenParenthesis(str)) {
						MatchAble mather = build(valuableMatchers, statement);
						String statementKey = "#" + index;
						valuableMatchers.put(statementKey, mather);
						stack.push(statementKey);
						break;
					} else {
						statement.push(str);
					}
					str = stack.pop();
				}
			} else {
				stack.push(tokens[index]);
			}
		}
		// 最终将剩下一个根元素为最终对象，如果stack中剩下不止一个元素则说明括号对不匹配
		if (stack.size() == 1) {
			matcher = valuableMatchers.get(stack.pop());
		} else {
			throw new MatchException(ErrorMessage.code("I-EXF-01-01-001", expression));
		}
		return matcher;
	}

	/**
	 * 将平行逻辑表达式构建为匹配器
	 * 
	 * @param valuableMatchers
	 *            matchers
	 * @param string
	 *            平行表达式
	 * @return
	 * @throws MatchException
	 */
	private static MatchAble build(Map<String, MatchAble> valuableMatchers, Stack<String> statement)
			throws MatchException {
		MatchAble matcher = null;
		String token = statement.pop();
		String statementKey = null;
		if (statement.size() == 0) {
			if (null == valuableMatchers.get(token)) {
				throw new MatchException(ErrorMessage.code("I-EXF-01-01-002", token));
			}
			return valuableMatchers.get(token);
		}
		while (StringUtils.isNotBlank(token)) {
			if (isLogicParenthesis(token)) {
				MatchAble left = null;
				if (null == matcher) {
					left = valuableMatchers.get(statementKey);
				} else {
					left = matcher;
				}
				if (null == left) {
					throw new MatchException(ErrorMessage.code("I-EXF-01-01-002", statementKey));
				}
				if (LOGIC_OR.equalsIgnoreCase(token)) {
					matcher = new OrLogicMatcher();
				} else {
					matcher = new AndLogicMatcher();
				}
				String rightKey = statement.pop();
				if (StringUtils.isBlank(rightKey)) {
					throw new MatchException(ErrorMessage.code("I-EXF-01-01-002", statementKey));
				}
				MatchAble right = valuableMatchers.get(rightKey);
				if (null == right) {
					throw new MatchException(ErrorMessage.code("I-EXF-01-01-002", rightKey));
				}
				((LogicMatcher) matcher).setLeft(left);
				((LogicMatcher) matcher).setRight(right);
			} else {
				statementKey = token;
			}
			if (statement.size() > 0) {
				token = statement.pop();
			} else {
				token = null;
			}
		}
		return matcher;
	}

	/**
	 * 提取 配置Matcher
	 * 
	 * @param rawMatchers
	 * @param valuableMatchers
	 */
	private static void extractRawMatchers(List<Matcher> rawMatchers, Map<String, MatchAble> valuableMatchers) {
		if (null != rawMatchers && !rawMatchers.isEmpty()) {
			for (Matcher matcher : rawMatchers) {
				valuableMatchers.put(matcher.getId(), matcher);
			}
		}
	}

	/**
	 * @Describe是否是一个右括号_
	 */
	private static boolean isCloseParenthesis(String c) {
		return BRACKET_RIGHT.equals(c);
	}

	/**
	 * @Describe_判断是否是一个左括号
	 */
	private static boolean isOpenParenthesis(String c) {
		return BRACKET_LEFT.equals(c);
	}

	private static boolean isLogicParenthesis(String c) {
		return LOGIC_OR.equalsIgnoreCase(c) || LOGIC_AND.equalsIgnoreCase(c);
	}

	public static void main(String[] args) throws MatchException {
		List<Matcher> rawMatchers = new ArrayList<>();
		rawMatchers.add(Matcher.of("m1", "expression", "1==1", new ExpressionMatcherProvider()));
		rawMatchers.add(Matcher.of("m2", "expression", "1==2", new ExpressionMatcherProvider()));
		rawMatchers.add(Matcher.of("m3", "expression", "1==1", new ExpressionMatcherProvider()));
		rawMatchers.add(Matcher.of("m4", "expression", "1==1", new ExpressionMatcherProvider()));
		rawMatchers.add(Matcher.of("m5", "expression", "1==2", new ExpressionMatcherProvider()));

		String expression = "m1 and (m2 or (m3 and m4)) or m5";
		MatchAble matcher = build(rawMatchers, expression);
		System.out.println(matcher);
		System.out.println(matcher.match(null));
	}
}
