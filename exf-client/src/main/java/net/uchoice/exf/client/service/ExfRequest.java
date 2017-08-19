package net.uchoice.exf.client.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ExfRequest {

	private String serviceCode;

	private String version;
	
	private String source;

	private Map<String, Object> params;
	
	/**
	 * 是否开启调试模式
	 * 调试模式下打印详细节点输入输出日志
	 */
	private boolean debug;
	
	/**
	 * 是否开启跟踪带
	 * 跟踪带将打印节点执行流水，debug模式将开启跟踪带
	 */
	private boolean trace;
	
	public ExfRequest(String serviceCode, String version) {
		this.serviceCode = serviceCode;
		this.version = version;
	}

	public ExfRequest(String serviceCode, String version, String source) {
		this.serviceCode = serviceCode;
		this.version = version;
		this.source = source;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public ExfRequest addParam(String key,Object value){
		if(null == this.params){
			this.params = new HashMap<String, Object>();
		}
		params.put(key, value);
		return this;
	}
	
	public boolean isDebug() {
		return debug;
	}

	public ExfRequest setDebug(boolean debug) {
		this.debug = debug;
		return this;
	}

	public String getSource() {
		return source;
	}

	public ExfRequest setSource(String source) {
		this.source = source;
		return this;
	}

	public boolean isTrace() {
		return trace;
	}

	public ExfRequest setTrace(boolean trace) {
		this.trace = trace;
		return this;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
