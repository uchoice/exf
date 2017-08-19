package net.uchoice.exf.core.config.parser.rule;

import java.util.UUID;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.core.config.exception.ActionConfigException;
import net.uchoice.exf.core.expression.groovy.GroovyLoader;
import net.uchoice.exf.core.runtime.invoker.ActionInvoker;
import net.uchoice.exf.model.action.ScriptAction;
import net.uchoice.exf.model.action.ScriptType;
import org.apache.commons.digester3.AbstractObjectCreationFactory;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;

public class ScriptActionCreatingFactory extends AbstractObjectCreationFactory<ScriptAction> {

	@Override
	public ScriptAction createObject(Attributes attributes) throws Exception {
		ScriptAction action = null;
		String name = null;
		String type = null;
		String uid = null;
		String match = null;
		int len = attributes.getLength();
		for (int i = 0; i < len; i++) {
			String qname = attributes.getQName(i);
			if (qname.equals("name")) {
				name = attributes.getValue(i);
			}
			if (qname.equals("type")) {
				type = attributes.getValue(i);
			}
			if (qname.equals("uid")) {
				uid = attributes.getValue(i);
			}
			if (qname.equals("match")) {
				match = attributes.getValue(i);
			}
		}
		if (StringUtils.isBlank(name) || StringUtils.isBlank(type) || StringUtils.isBlank(uid)) {
			throw new ActionConfigException(ErrorMessage.code("S-EXF-01-01-006", name, type, uid));
		}
		if(ScriptType.of(type) == null){
			throw new ActionConfigException(ErrorMessage.code("S-EXF-01-01-007", name, type, uid));
		}

		String scriptName = UUID.randomUUID().toString();
		// groovy脚本需要预先类加载
		if (ScriptType.of(type) == ScriptType.GROOVY) {
			action = new ScriptAction(scriptName, (sa) -> {
				GroovyLoader.instance().load(sa.getExpression(), scriptName);
			});
		} else {
			action = new ScriptAction();
		}
		action.setUid(uid);
		action.setName(name);
		action.setInvoker(new ActionInvoker());
		action.setType(ScriptType.of(type));
		if(StringUtils.isNotBlank(match)){
			action.setMatchExpression(match);
		}
		return action;
	}
}
