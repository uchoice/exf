package net.uchoice.exf.model.action;

public enum ActionState {

	/**
	 * 节点匹配器不匹配
	 */
	NO_MATCH(1), 
	/**
	 * 执行成功
	 */
	SUCCESS(0), 
	/**
	 * 执行失败 
	 */
	FAIL(-1), 
	/**
	 * 异常 
	 */
	ERROR(-2);

	private int code;

	private ActionState(int code) {
		this.code = code;
	}
	
	public boolean isSuccess(){
		return this.code >= 0;
	}

	public int getCode() {
		return code;
	}
}
