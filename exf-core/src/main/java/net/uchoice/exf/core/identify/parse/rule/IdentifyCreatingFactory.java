package net.uchoice.exf.core.identify.parse.rule;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.core.identify.Identify;
import net.uchoice.exf.core.identify.exception.IdentifyConfigException;
import org.apache.commons.digester3.AbstractObjectCreationFactory;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;

public class IdentifyCreatingFactory extends AbstractObjectCreationFactory<Identify> {

	@Override
	public Identify createObject(Attributes attributes) throws Exception {
		String id = null;
		String name = null;
		String match = null;
		String priority = null;
		int len = attributes.getLength();
		for (int i = 0; i < len; i++) {
			String qname = attributes.getQName(i);
			if (qname.equals("id")) {
				id = attributes.getValue(i);
			}
			if (qname.equals("name")) {
				name = attributes.getValue(i);
			}
			if (qname.equals("match")) {
				match = attributes.getValue(i);
			}
			if (qname.equals("priority")) {
				priority = attributes.getValue(i);
			}
		}
		if (StringUtils.isBlank(id) || StringUtils.isBlank(name) || StringUtils.isBlank(match)
				|| StringUtils.isBlank(priority)) {
			throw new IdentifyConfigException(ErrorMessage.code("I-EXF-01-01-003", id, name, match, priority));
		}
		return Identify.of(id, name, match, Integer.parseInt(priority));
	}
}
