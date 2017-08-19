package net.uchoice.exf.core.identify;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

public class IdentifyManager {

	private static IdentifyManager instance = new IdentifyManager();
	
	private Map<String, Identify> identifyMap = new ConcurrentHashMap<>();

	private IdentifyManager() {
		super();
	}

	public static IdentifyManager getInstance() {
		return instance;
	}

	public Map<String, Identify> getIdentifyMap() {
		return identifyMap;
	}

	public void setIdentifyMap(Map<String, Identify> identifyMap) {
		this.identifyMap = identifyMap;
	}
	
	public void addIdentify(Identify identify) {
		if (null == identify) {
			return;
		}
		this.identifyMap.put(identify.getId(), identify);
	}
	
	public boolean contain(Identify identify){
		if (null == identify || StringUtils.isBlank(identify.getId())) {
			return false;
		}
		return this.identifyMap.containsKey(identify.getId());
	}
}
