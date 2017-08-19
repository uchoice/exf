package net.uchoice.exf.core.loader.cache;

import net.uchoice.exf.core.cache.SimpleCache;
import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.action.ActionSpec;
import net.uchoice.exf.model.container.ContainerInst;
import net.uchoice.exf.model.container.ContainerSpec;

public class PluginCache extends SimpleCache {

    private static final String CACHE_CONTAINER = "exf.container";
    
    private static final String CACHE_CONTAINER_SPEC = "exf.container.spec";
    
    private static final String CACHE_ACTION = "exf.action";
    
    private static final String CACHE_ACTION_SPEC = "exf.action.spec";

    public void cacheContainerSpec(ContainerSpec container) {
        if(null != container){
        	doCacheObject(CACHE_CONTAINER_SPEC, container.getCode(), container);
        }
    }
    
    public ContainerSpec getCachedContainerSpec(String code){
    	return getCachedEntity(CACHE_CONTAINER_SPEC, code);
    }
    
    public boolean isContainerSpecRegisted(String code) {
    	return getCachedContainerSpec(code) != null;
    }
    
    public void cacheContainer(ContainerInst container) {
        if(null != container){
        	doCacheObject(CACHE_CONTAINER, container.getUid(), container);
        }
    }
    
    public ContainerSpec getCachedContainer(String uid){
    	return getCachedEntity(CACHE_CONTAINER, uid);
    }
    
    public void cacheActionSpec(ActionSpec container) {
        if(null != container){
        	doCacheObject(CACHE_ACTION_SPEC, container.getCode(), container);
        }
    }
    
    public ActionSpec getCachedActionSpec(String code){
    	return getCachedEntity(CACHE_ACTION_SPEC, code);
    }
    
    public void cacheAction(ActionInst container) {
        if(null != container){
        	doCacheObject(CACHE_ACTION, container.getUid(), container);
        }
    }
    
    public ActionSpec getCachedAction(String uid){
    	return getCachedEntity(CACHE_ACTION, uid);
    }

}
