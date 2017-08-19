package net.uchoice.exf.core.config;

public class ServiceConfig {
	
	private String id;
	
	private String label;
	
	private ServiceConfigType type;
	
	private String options;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ServiceConfigType getType() {
		return type;
	}

	public void setType(String type) {
		this.type = ServiceConfigType.of(type);
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}
	
	public static ServiceConfig of(String id, String label, String type, String options){
		ServiceConfig serviceConfig = new ServiceConfig();
		serviceConfig.setId(id);
		serviceConfig.setLabel(label);
		serviceConfig.setOptions(options);
		serviceConfig.setType(type);
		return serviceConfig;
	}
}
