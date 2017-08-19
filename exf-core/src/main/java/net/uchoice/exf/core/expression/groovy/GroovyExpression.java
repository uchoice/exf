package net.uchoice.exf.core.expression.groovy;

import java.util.HashMap;
import java.util.Map;

import groovy.lang.Binding;

public class GroovyExpression {

	/**
	 * 输出对象key，表达式中需要输出信息out.put('key','value')
	 */
	private static final String OUT_KEY = "out";

	private GroovyShell shell;

	private Binding binding;

	private Map<String, Object> out = new HashMap<>();

	public GroovyExpression() {
		this(null);
	}

	public GroovyExpression(Map<String, Object> context) {
		init(context);
		shell = new GroovyShell(GroovyLoader.instance().getClassLoader(), binding);
	}

	public void init(Map<String, Object> context) {
		binding = new Binding();
		binding.setVariable(OUT_KEY, out);
		if (null != context && !context.isEmpty()) {
			context.forEach((k, v) -> {
				binding.setVariable(k, v);
			});
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> evaluate(String expression) {
		shell.evaluate(expression);
		return (Map<String, Object>) binding.getVariable(OUT_KEY);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> evaluate(String expression, String scriptName) {
		shell.evaluate(expression, scriptName);
		return (Map<String, Object>) binding.getVariable(OUT_KEY);
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		System.out.println(String.class.isAssignableFrom(String.class));
		Map<String, Object> map = new HashMap<>();
		User u = new User();
		u.setName("mrl");
		u.setAge(27);
		map.put("user", u);
		map.put("name", "lv");

		// 从Java代码中调用Groovy语句
		Binding binding = new Binding();
		binding.setVariable("foo", new Integer(2));
		binding.setVariable("context", map);
		GroovyShell shell = new GroovyShell(binding);
		String script = "" + "println context.get('a.name'); context.get('user').setAge(10); a=123;"
				+ "context.put('gu',context.get('user'));return foo * 10";
		Object value = shell.evaluate(script);
		Map m = (Map) binding.getVariable("context");
		System.out.println(value);
		User gu = (User) m.get("gu");
		System.out.println(gu.getName() + ":" + gu.getAge());
		binding.getVariables();
		assert value.equals(new Integer(20));
		assert binding.getVariable("x").equals(new Integer(123));
	}
}

class User {
	private String name;

	private Integer age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
