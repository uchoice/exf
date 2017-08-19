package net.uchoice.exf.app.common.action;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.annotation.Action;

@Action(code = NullCheck.CODE, method = "check", name = "空判断")
public class NullCheck extends ActionInst {

	public static final String CODE = "net.uchoice.exf.app.common.action.NullCheck";
	
	public boolean check(Object obj) {
		if (obj instanceof List) {
			return notNull((List<?>) obj);
		}
		if (obj instanceof String) {
			return notNull((String) obj);
		}
		return null != obj;
	}

	private boolean notNull(String string) {
		return null != string && string.length() > 0;
	}

	private boolean notNull(List<?> list) {
		return null != list && !list.isEmpty();
	}
	
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method[] ms = NullCheck.class.getDeclaredMethods();
		for(Method m:ms){
			if(m.getName().equals("notNull1")){
				System.out.println(m.getReturnType());
				//System.out.println(m.getGenericParameterTypes()[0].getTypeName());
				//System.out.println(((ParameterizedType)m.getGenericParameterTypes()[0]).getActualTypeArguments()[0].getTypeName());
				Parameter[] ps = m.getParameters();
				for(Parameter p:ps){
					System.out.println(p.getName());
					System.out.println(p.getType());
					if (p.getParameterizedType() instanceof ParameterizedType) {
						System.out.println(((ParameterizedType)p.getParameterizedType()).getActualTypeArguments()[0].getTypeName());
					}
				} 
				System.out.println(m.invoke(new NullCheck()));
			}
		}
		
		Field[] fields = NullCheck.class.getDeclaredFields();
		if(null != fields && fields.length >0){
			for (int index = 0; index < fields.length; index++) {
				System.out.println(fields[index].getName());
				System.out.println(fields[index].getType());
				System.out.println(fields[index].getGenericType());
			}
		}
	}
}
