package net.uchoice.exf.model.action;

import java.util.HashMap;
import java.util.Map;

public class ActionResult {

	public static final String MODEL_ERROR_CAUSE = "_err_cause";
	
	public static final String MODEL_ERROR_MESSAGE = "_err_msg";

	private ActionState state = ActionState.SUCCESS;
	
	private Object result;

	private Map<String, Object> model = new HashMap<String, Object>();

	public static ActionResult buildErrorResult(Throwable e) {
		return buildErrorResult(e, e.getMessage());
	}
	
	public static ActionResult buildErrorResult(Throwable e, String errMessage) {
		ActionResult result = new ActionResult(ActionState.ERROR);
		result.put(MODEL_ERROR_CAUSE, e);
		result.put(MODEL_ERROR_MESSAGE, errMessage);
		return result;
	}
	
	public String getErrMessage(){
		return String.valueOf(this.model.get(MODEL_ERROR_MESSAGE));
	}
	
	public Throwable getError() {
		return (Throwable) this.model.get(MODEL_ERROR_CAUSE);
	}

	public ActionResult() {
	};

	public ActionResult(ActionState state) {
		this.state = state;
	}

	public ActionState getState() {
		return state;
	}

	public void setState(ActionState state) {
		this.state = state;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void putAll(Map<String, Object> model) {
		this.model.putAll(model);
	}

	public void put(String key, Object value) {
		this.model.put(key, value);
	}

	public Object get(String key) {
		return this.model.get(key);
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
