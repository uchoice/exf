package net.uchoice.exf.core.loader;

import org.springframework.beans.factory.FactoryBean;

/**
 * 插件管理器
 */
public class PluginManagerLoader implements FactoryBean<PluginManager> {

    @Override
    public PluginManager getObject() throws Exception {
        return PluginManager.getInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return PluginManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
