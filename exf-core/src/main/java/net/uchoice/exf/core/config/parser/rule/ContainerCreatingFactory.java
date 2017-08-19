package net.uchoice.exf.core.config.parser.rule;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.core.config.exception.ContainerConfigException;
import net.uchoice.exf.core.loader.PluginManager;
import net.uchoice.exf.model.container.ContainerInst;
import net.uchoice.exf.model.container.ContainerSpec;
import org.apache.commons.digester3.AbstractObjectCreationFactory;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;

public class ContainerCreatingFactory extends AbstractObjectCreationFactory<ContainerInst> {

	@Override
	public ContainerInst createObject(Attributes attributes) throws Exception {
		ContainerInst container = null;
		String name = null;
		String code = null;
		String uid = null;
		int len = attributes.getLength();
		for (int i = 0; i < len; i++) {
			String qname = attributes.getQName(i);
			if (qname.equals("name")) {
				name = attributes.getValue(i);
			}
			if (qname.equals("code")) {
				code = attributes.getValue(i);
			}
			if (qname.equals("uid")) {
				uid = attributes.getValue(i);
			}
		}
		if (StringUtils.isBlank(name) || StringUtils.isBlank(code) || StringUtils.isBlank(uid)) {
			throw new ContainerConfigException(ErrorMessage.code("S-EXF-01-01-002", name, code, uid));
		}

		ContainerSpec containerSpec = PluginManager.getInstance().getContainerSpec(code);
		if (null == containerSpec) {
			throw new ContainerConfigException(ErrorMessage.code("S-EXF-01-01-003", code));
		}
		container = containerSpec.getType().newInstance();
		container.setUid(uid);
		container.setName(name);
		container.setSpec(containerSpec);
		return container;
	}
}
