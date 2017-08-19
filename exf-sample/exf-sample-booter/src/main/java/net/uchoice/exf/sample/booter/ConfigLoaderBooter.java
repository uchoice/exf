package net.uchoice.exf.sample.booter;
import java.io.IOException;

import net.uchoice.exf.core.config.ServiceManager;
import net.uchoice.exf.client.exception.ExfException;
import net.uchoice.exf.core.config.ResourceBasedConfig;

public class ConfigLoaderBooter {

	public static ResourceBasedConfig loadResourceBasedConfig() {
		return new ResourceBasedConfig();
	}

	public static void main(String[] args) throws IOException, ExfException {
		
		PluginLoaderBooter.loadPlugin();
		
		ResourceBasedConfig resourceBasedConfig = loadResourceBasedConfig();
		resourceBasedConfig.init();
		ServiceManager.getInstance().getServiceMap().forEach((k,v)->{
			System.out.println(k);
		});
	}
}