package net.uchoice.exf.sample.booter;

import java.io.IOException;

import net.uchoice.exf.client.exception.ExfException;
import net.uchoice.exf.client.service.ExfRequest;
import net.uchoice.exf.client.service.ExfResponse;
import net.uchoice.exf.core.service.impl.ExfRuntimeServiceImpl;

public class AppBooter {

	public static void main(String[] args) throws IOException, NumberFormatException, ExfException {
		PluginLoaderBooter.loadPlugin();
		ConfigLoaderBooter.loadResourceBasedConfig().init();
		IdentifyLoaderBooter.loadResourceBasedIdentify().init();

		execute(args == null || args.length < 1 ? 1000L : Long.valueOf(args[0]));
	}

	private static void execute(Long sleep) throws ExfException {
		if (null == sleep || sleep < 1000) {
			sleep = 5000L;
		}
		ExfRequest request = new ExfRequest("user.name.modify", "1.0.0");
		request.addParam("id", 2).addParam("name", "Mrl")
		;
		//for (;;) {
			long time = System.currentTimeMillis();
			ExfResponse response = new ExfRuntimeServiceImpl().request(request);
			System.out.println("Cost:" + (System.currentTimeMillis() - time));
			System.out.println(response.isSuccess());
			if (response.isSuccess()) {
				response.getResult().forEach((k, v) -> {
					System.out.println(k + ":" + v);
				});
			} else {
				System.out.println(response.getErrCode() + ":" + response.getErrMessage());
			}
			System.out.println("");
			System.out.println("------------------------------------------------------------------------------");
			System.out.println("");
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}
}
