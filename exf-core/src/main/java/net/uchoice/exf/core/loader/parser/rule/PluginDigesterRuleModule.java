package net.uchoice.exf.core.loader.parser.rule;

import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;

public class PluginDigesterRuleModule extends FromXmlRulesModule {

    @Override
    protected void loadRules() {
        this.loadXMLRules(PluginDigesterRuleModule.class.getResource("/digester/plugin-digester-rules.xml"));
    }
}
