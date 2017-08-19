package net.uchoice.exf.sample.booter;
import java.io.IOException;

import net.uchoice.exf.client.exception.ExfException;
import net.uchoice.exf.core.loader.PluginManager;

public class PluginLoaderBooter {
	
	public static PluginManager loadPlugin() throws ExfException{
		return PluginManager.getInstance();
	}

	public static void main(String[] args) throws IOException, ExfException {
		PluginManager pluginManager = loadPlugin();
		pluginManager.getPluginMap().forEach((k,v)->{
			System.out.println(k);
		});
	}
}
