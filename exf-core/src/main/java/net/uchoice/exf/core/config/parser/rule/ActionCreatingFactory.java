package net.uchoice.exf.core.config.parser.rule;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.core.config.exception.ActionConfigException;
import net.uchoice.exf.core.loader.PluginManager;
import net.uchoice.exf.core.runtime.invoker.ActionInvoker;
import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.ActionSpec;
import org.apache.commons.digester3.AbstractObjectCreationFactory;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;

public class ActionCreatingFactory extends AbstractObjectCreationFactory<ActionInst> {

	@Override
	public ActionInst createObject(Attributes attributes) throws Exception {
		ActionInst action = null;
		String name = null;
		String code = null;
		String uid = null;
		String match = null;
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
			if (qname.equals("match")) {
				match = attributes.getValue(i);
			}
		}
		if (StringUtils.isBlank(name) || StringUtils.isBlank(code) || StringUtils.isBlank(uid)) {
			throw new ActionConfigException(ErrorMessage.code("S-EXF-01-01-004", name, code, uid));
		}

		ActionSpec actionSpec = PluginManager.getInstance().getActionSpec(code);
		if (null == actionSpec) {
			throw new ActionConfigException(ErrorMessage.code("S-EXF-01-01-005", code));
		}
		action = actionSpec.getType().newInstance();
		action.setUid(uid);
		action.setName(name);
		action.setInvoker(new ActionInvoker());
		action.setSpec(actionSpec);
		if(StringUtils.isNotBlank(match)){
			action.setMatchExpression(match);
		}
		return action;
	}
}
