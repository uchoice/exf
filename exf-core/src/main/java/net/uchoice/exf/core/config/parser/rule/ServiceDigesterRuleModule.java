package net.uchoice.exf.core.config.parser.rule;

import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;

public class ServiceDigesterRuleModule extends FromXmlRulesModule {

    @Override
    protected void loadRules() {
        this.loadXMLRules(ServiceDigesterRuleModule.class.getResource("/digester/service-digester-rules.xml"));
    }
}
