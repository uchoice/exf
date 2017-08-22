package net.uchoice.exf.client.service;

import java.util.Map;

public class ExfResponse {
	
	public static final String RESULT_KEY = "_result";
	
	private boolean success = true;
	
	private int resultCode;
	
	private String errCode;
	
	private String errMessage;
	
	private Map<String, Object> result;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Map<String, Object> getResults() {
		return result;
	}
	
	public Object getResult() {
		return result == null ? null : result.get(RESULT_KEY);
	}
	
	public Object getResult(String key) {
		return result == null ? null : result.get(key);
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
	
	public Object getModule(String name){
		return result.get(name);
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
}
