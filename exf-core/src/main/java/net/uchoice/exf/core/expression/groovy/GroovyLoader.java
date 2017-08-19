package net.uchoice.exf.core.expression.groovy;

import java.security.AccessController;
import java.security.PrivilegedAction;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;

public class GroovyLoader {

	private static GroovyClassLoader loader = new GroovyClassLoader(GroovyLoader.class.getClassLoader());

	private static GroovyLoader instance = new GroovyLoader();

	private GroovyLoader() {
		super();
	}

	public static GroovyLoader instance() {
		return instance;
	}

	public Class<?> load(String script, String scriptName) {
		GroovyCodeSource gcs = AccessController.doPrivileged(new PrivilegedAction<GroovyCodeSource>() {
			public GroovyCodeSource run() {
				return new GroovyCodeSource(script, scriptName, "/groovy/script");
			}
		});
		gcs.setCachable(true);
		return loader.parseClass(gcs);
	}

	public GroovyClassLoader getClassLoader() {
		return loader;
	}
}
