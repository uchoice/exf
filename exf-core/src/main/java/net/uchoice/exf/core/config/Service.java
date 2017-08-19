package net.uchoice.exf.core.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.uchoice.exf.core.identify.Identify;
import net.uchoice.exf.core.matcher.Matcher;
import net.uchoice.exf.model.container.ContainerInst;

/**
 * 配置服务实例
 *
 */
public class Service {

	private String name;
	
	private String code;
	
	private String version;
	
	private List<ContainerInst> containers = new ArrayList<>();
	
	/**
	 * 服务配置扩展点,由业务身份来实现配置
	 */
	private List<ServiceConfig> configs = new ArrayList<>();
	
	/**
	 * 匹配器，用于节点匹配
	 */
	private List<Matcher> matchers = new ArrayList<>();
	
	/**
	 * 支持身份列表，按优先级降序
	 */
	private List<Identify> supportIdentifys = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<ContainerInst> getContainers() {
		return containers;
	}

	public void setContainers(List<ContainerInst> containers) {
		this.containers = containers;
	}
	
	public void addContainer(ContainerInst container) {
		this.containers.add(container);
	}

	public List<ServiceConfig> getConfigs() {
		return configs;
	}

	public void setConfigs(List<ServiceConfig> configs) {
		this.configs = configs;
	}
	
	public void addConfig(ServiceConfig config) {
		this.configs.add(config);
	}

	public List<Matcher> getMatchers() {
		return matchers;
	}

	public void setMatchers(List<Matcher> matchers) {
		this.matchers = matchers;
	}
	
	public void addMatcher(Matcher matcher) {
		this.matchers.add(matcher);
	}

	public List<Identify> getSupportIdentifys() {
		return supportIdentifys;
	}

	public void addSupportIdentify(Identify identify) {
		if(null != identify){
			synchronized (this) {
				this.supportIdentifys.add(identify);
				Collections.sort(this.supportIdentifys, new Comparator<Identify>() {
					@Override
					public int compare(Identify o1, Identify o2) {
						if (o1.getPriority() >= o2.getPriority()) {
							return -1;
						} else {
							return 1;
						}
					}
				});
			}
		}
	}
}
