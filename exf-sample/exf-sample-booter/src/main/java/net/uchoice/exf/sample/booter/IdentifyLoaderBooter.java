package net.uchoice.exf.sample.booter;

import java.io.IOException;

import net.uchoice.exf.client.exception.ExfException;
import net.uchoice.exf.core.identify.IdentifyManager;
import net.uchoice.exf.core.identify.ResourceBasedIdentify;

public class IdentifyLoaderBooter {

	public static ResourceBasedIdentify loadResourceBasedIdentify() {
		return new ResourceBasedIdentify();
	}

	public static void main(String[] args) throws IOException, ExfException {

		PluginLoaderBooter.loadPlugin();
		ConfigLoaderBooter.loadResourceBasedConfig().init();

		ResourceBasedIdentify resourceBasedConfig = loadResourceBasedIdentify();
		resourceBasedConfig.init();
		IdentifyManager.getInstance().getIdentifyMap().forEach((k, v) -> {
			System.out.println(k);
		});
	}
}