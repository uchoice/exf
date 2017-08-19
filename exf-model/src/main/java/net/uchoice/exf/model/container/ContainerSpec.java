package net.uchoice.exf.model.container;

import net.uchoice.exf.model.common.BaseSpec;

public class ContainerSpec extends BaseSpec {

	private Class<? extends ContainerInst> type;

	public Class<? extends ContainerInst> getType() {
		return type;
	}

	public void setType(Class<? extends ContainerInst> type) {
		this.type = type;
	}
	
	public static ContainerSpec of(String plugin, String code, String name, Class<? extends ContainerInst> type){
		ContainerSpec containerSpec = new ContainerSpec();
		containerSpec.setPlugin(plugin);
		containerSpec.setCode(code);
		containerSpec.setName(name);
		containerSpec.setType(type);
		return containerSpec;
	}
}
