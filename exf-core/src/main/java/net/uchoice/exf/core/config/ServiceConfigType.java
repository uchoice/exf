package net.uchoice.exf.core.config;

public enum ServiceConfigType {

	/**
	 * 枚举类型，从选项中选择一个值
	 */
	ENUM,
	/**
	 * 多值类型，从选项中选择多个值
	 */
	MULTI,
	/**
	 * 文本类型，从输入中获取值
	 */
	INPUT;

	public static ServiceConfigType of(String type) {
		for (ServiceConfigType configType : ServiceConfigType.values()) {
			if (configType.name().equalsIgnoreCase(type)) {
				return configType;
			}
		}
		return null;
	}
}
