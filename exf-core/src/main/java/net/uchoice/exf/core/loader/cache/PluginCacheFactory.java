package net.uchoice.exf.core.loader.cache;

import org.springframework.beans.factory.FactoryBean;

public class PluginCacheFactory implements FactoryBean<PluginCache> {

    @Override
    public PluginCache getObject() throws Exception {
        return new PluginCache();
    }

    @Override
    public Class<?> getObjectType() {
        return PluginCache.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
