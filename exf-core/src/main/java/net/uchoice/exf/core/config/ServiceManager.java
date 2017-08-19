package net.uchoice.exf.core.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceManager {

	private static ServiceManager instance = new ServiceManager();

	private Map<String, Service> serviceMap = new ConcurrentHashMap<>();

	private ServiceManager() {
		super();
	}

	public static ServiceManager getInstance() {
		return instance;
	}

	public void addServiceConfig(Service serviceConfig) {
		if (null == serviceConfig) {
			return;
		}
		serviceMap.put(buildServiceKey(serviceConfig), serviceConfig);
	}

	public Map<String, Service> getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(Map<String, Service> serviceMap) {
		this.serviceMap = serviceMap;
	}
	
	public Service getService(String serviceCode, String version){
		return this.serviceMap.get(buildServiceKey(serviceCode, version));
	}

	private String buildServiceKey(Service serviceConfig) {
		return buildServiceKey(serviceConfig.getCode(), serviceConfig.getVersion());
	}
	
	public static String buildServiceKey(String serviceCode, String version) {
		return new StringBuilder(serviceCode).append("@").append(version).toString();
	}
}
