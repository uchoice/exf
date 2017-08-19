package net.uchoice.exf.core.identify.parse.rule;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.core.identify.IdentifyConfig;
import net.uchoice.exf.core.identify.exception.IdentifyConfigException;
import org.apache.commons.digester3.AbstractObjectCreationFactory;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;

public class IdentifyConfigCreatingFactory extends AbstractObjectCreationFactory<IdentifyConfig> {

	@Override
	public IdentifyConfig createObject(Attributes attributes) throws Exception {
		String id = null;
		String value = null;
		int len = attributes.getLength();
		for (int i = 0; i < len; i++) {
			String qname = attributes.getQName(i);
			if (qname.equals("id")) {
				id = attributes.getValue(i);
			}
			if (qname.equals("value")) {
				value = attributes.getValue(i);
			}
		}
		if (StringUtils.isBlank(id) || StringUtils.isBlank(value)) {
			throw new IdentifyConfigException(ErrorMessage.code("I-EXF-01-01-004", id, value));
		}
		return IdentifyConfig.of(id, value);
	}
}
