package net.uchoice.exf.model.plugin;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Plugin {

	private String key;

	private String name;

	private String version;

	private String system;

	private String description;
	
	private String packages;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	public String[] getAllPackages() {
		if(null != packages){
			return packages.split(",");
		}
		return null;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
