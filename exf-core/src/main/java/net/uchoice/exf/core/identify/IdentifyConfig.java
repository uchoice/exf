package net.uchoice.exf.core.identify;

import com.alibaba.fastjson.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class IdentifyConfig {

	private String id;

	private Object value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public static IdentifyConfig of(String id, String value) {
		IdentifyConfig config = new IdentifyConfig();
		config.setId(id);
		config.setValue(parseValue(value));
		return config;
	}

	/**
	 * 值对象转化
	 * 
	 * @param value
	 *            value字符串
	 * @return 字符串或字符数组
	 */
	private static Object parseValue(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		if (value.startsWith("[") && value.endsWith("]")) {
			return JSONArray.parseArray(value).toArray(new String[] {});
		} else {
			return value;
		}
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
