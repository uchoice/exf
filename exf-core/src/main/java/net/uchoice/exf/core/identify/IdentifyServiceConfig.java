package net.uchoice.exf.core.identify;

import java.util.HashMap;
import java.util.Map;

import net.uchoice.exf.core.config.ServiceManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 业务身份服务扩展
 *
 */
public class IdentifyServiceConfig {

	/**
	 * 服务代码
	 */
	private String code;

	/**
	 * 名称
	 */
	private String version;

	/**
	 * 业务扩展配置点
	 */
	private Map<String, IdentifyConfig> configs = new HashMap<>();

	public Map<String, IdentifyConfig> getConfigs() {
		return configs;
	}

	public void setConfigs(Map<String, IdentifyConfig> configs) {
		this.configs = configs;
	}

	public IdentifyServiceConfig addConfig(IdentifyConfig config) {
		if (null != config && StringUtils.isNotBlank(config.getId())) {
			this.configs.put(config.getId(), config);
		}
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public static IdentifyServiceConfig of(String code, String version) {
		IdentifyServiceConfig config = new IdentifyServiceConfig();
		config.setCode(code);
		config.setVersion(version);
		return config;
	}

	public String getServiceKey() {
		return ServiceManager.buildServiceKey(this.code, this.version);
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
