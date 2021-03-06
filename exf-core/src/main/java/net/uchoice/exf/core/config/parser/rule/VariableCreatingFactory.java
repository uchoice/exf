package net.uchoice.exf.core.config.parser.rule;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.core.config.exception.VariableConfigException;
import net.uchoice.exf.core.runtime.resolver.InVariableResolver;
import net.uchoice.exf.core.runtime.resolver.PropVariableResolver;
import net.uchoice.exf.model.variable.Variable;
import net.uchoice.exf.model.variable.VariableMode;
import net.uchoice.exf.model.variable.VariableSpec;
import org.apache.commons.digester3.AbstractObjectCreationFactory;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;

public class VariableCreatingFactory extends AbstractObjectCreationFactory<Variable> {

	@Override
	public Variable createObject(Attributes attributes) throws Exception {
		Variable variable = new Variable();
		String name = null;
		String mode = null;
		String value = null;
		int len = attributes.getLength();
		for (int i = 0; i < len; i++) {
			String qname = attributes.getQName(i);
			if (qname.equals("name")) {
				name = attributes.getValue(i);
			}
			if (qname.equals("mode")) {
				mode = attributes.getValue(i);
			}
			if (qname.equals("value")) {
				value = attributes.getValue(i);
			}
		}
		if (StringUtils.isBlank(name) || StringUtils.isBlank(mode) || StringUtils.isBlank(value)) {
			throw new VariableConfigException(ErrorMessage.code("S-EXF-01-01-008", name, mode, value));
		}
		VariableSpec variableSpec = new VariableSpec();
		variableSpec.setMode(VariableMode.of(mode));
		variableSpec.setName(name);
		variable.setValue(value);
		variable.setVariableSpec(variableSpec);
		if (VariableMode.of(mode) == VariableMode.PROP) {
			variable.setResolver(PropVariableResolver.get());
		}
		if (VariableMode.of(mode) == VariableMode.IN) {
			variable.setResolver(InVariableResolver.get());
		}
		return variable;
	}
}
