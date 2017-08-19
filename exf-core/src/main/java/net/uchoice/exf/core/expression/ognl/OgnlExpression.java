package net.uchoice.exf.core.expression.ognl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class OgnlExpression {

	private OgnlContext context;

	public OgnlExpression() {
		this(null);
	}

	public OgnlExpression(Map<String, Object> context) {
		this.context = new OgnlContext(context);
	}

	public Object evaluate(String expression) throws OgnlException {
		return Ognl.getValue(expression, this.context, new Object());
	}
	
	@SuppressWarnings("unchecked")
	public <T> T evaluate(String expression, Class<T> cls) throws OgnlException {
		return (T) Ognl.getValue(expression, this.context, new Object(), cls);
	}
	
	public static void main(String[] args) throws OgnlException {
		Map<String, Object> context = new HashMap<String, Object>();

		Person person1 = new Person();
		person1.setName("zhangsan");

		Person person2 = new Person();
		person2.setName("lisi");

		Person person3 = new Person();
		person3.setName("wangwu");

		Person person4 = new Person();
		person4.setName("zhaoliu");

		List<Person> persons = new ArrayList<>();
		persons.add(person1);
		persons.add(person2);
		persons.add(person3);
		persons.add(person4);

		context.put("person1", person1);
		context.put("person2_p", person2);
		context.put("person3", person3);
		context.put("persons", persons);

		Object value = Ognl.getValue("name", context, person2);
		System.out.println("ognl expression \"name\" evaluation is : " + value);

		Object value2 = Ognl.getValue("#person2_p.name", context, person2);
		System.out.println("ognl expression \"#person2.name\" evaluation is : " + value2);

		Object value4 = Ognl.getValue("name", context, person4);
		System.out.println("ognl expression \"name\" evaluation is : " + value4);

		Object value5 = Ognl.getValue("#persons.size() == 4 && (1>2 || 4 in {1,2,3,#persons.size()})", context,
				new Object());
		System.out.println("ognl expression \"name\" evaluation is : " + value5);
	}
}

class Person {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
