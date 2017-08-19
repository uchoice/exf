package net.uchoice.exf.core.identify.parse.rule;

import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;

public class IdentifyDigesterRuleModule extends FromXmlRulesModule {

    @Override
    protected void loadRules() {
        this.loadXMLRules(IdentifyDigesterRuleModule.class.getResource("/digester/identify-digester-rules.xml"));
    }
}
