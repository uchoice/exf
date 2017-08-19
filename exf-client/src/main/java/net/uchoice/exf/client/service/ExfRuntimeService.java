package net.uchoice.exf.client.service;

public interface ExfRuntimeService {

	/**
	 * exf服务调用
	 * @param request
	 * @return
	 */
	ExfResponse request(ExfRequest request);
}
