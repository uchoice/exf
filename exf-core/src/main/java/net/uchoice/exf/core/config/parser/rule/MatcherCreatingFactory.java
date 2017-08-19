package net.uchoice.exf.core.config.parser.rule;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.core.config.exception.ServiceConfigException;
import net.uchoice.exf.core.matcher.Matcher;
import net.uchoice.exf.core.matcher.provider.MatchProvider;
import net.uchoice.exf.core.matcher.provider.MatchProviderFactory;
import org.apache.commons.digester3.AbstractObjectCreationFactory;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;

public class MatcherCreatingFactory extends AbstractObjectCreationFactory<Matcher> {

	@Override
	public Matcher createObject(Attributes attributes) throws Exception {
		String id = null;
		String type = null;
		String test = null;
		MatchProvider provider = null;
		int len = attributes.getLength();
		for (int i = 0; i < len; i++) {
			String qname = attributes.getQName(i);
			if (qname.equals("id")) {
				id = attributes.getValue(i);
			}
			if (qname.equals("type")) {
				type = attributes.getValue(i);
			}
			if (qname.equals("test")) {
				test = attributes.getValue(i);
			}
		}
		if (StringUtils.isBlank(id) || StringUtils.isBlank(test) || StringUtils.isBlank(type)) {
			throw new ServiceConfigException(ErrorMessage.code("S-EXF-01-01-009", id, test, type));
		}
		provider = MatchProviderFactory.create(type);
		if (null == provider) {
			throw new ServiceConfigException(ErrorMessage.code("S-EXF-01-01-010", id, test, type));
		}
		return Matcher.of(id, type, test, provider);
	}
}
