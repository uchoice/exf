package net.uchoice.exf.core.config.parser.rule;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.core.config.ServiceConfig;
import net.uchoice.exf.core.config.exception.ServiceConfigException;
import org.apache.commons.digester3.AbstractObjectCreationFactory;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;

public class ServiceConfigCreatingFactory extends AbstractObjectCreationFactory<ServiceConfig> {

	@Override
	public ServiceConfig createObject(Attributes attributes) throws Exception {
		String id = null;
		String label = null;
		String type = null;
		String options = null;
		int len = attributes.getLength();
		for (int i = 0; i < len; i++) {
			String qname = attributes.getQName(i);
			if (qname.equals("id")) {
				id = attributes.getValue(i);
			}
			if (qname.equals("label")) {
				label = attributes.getValue(i);
			}
			if (qname.equals("type")) {
				type = attributes.getValue(i);
			}
			if (qname.equals("options")) {
				options = attributes.getValue(i);
			}
		}
		if (StringUtils.isBlank(id) || StringUtils.isBlank(label) || StringUtils.isBlank(type)) {
			throw new ServiceConfigException(ErrorMessage.code("S-EXF-01-01-001", id, label, type));
		}
		
		return ServiceConfig.of(id, label, type, options);
	}
}
