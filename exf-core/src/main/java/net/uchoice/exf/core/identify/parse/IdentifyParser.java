package net.uchoice.exf.core.identify.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.annotation.Nonnull;

import net.uchoice.exf.core.identify.Identify;
import net.uchoice.exf.core.identify.parse.rule.IdentifyDigesterRuleModule;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.xml.sax.SAXException;

/**
 * 服务配置加载.
 *
 */
public class IdentifyParser {

	/**
	 * the digester for parsing the XML.
	 */
	private static Digester digester = null;

	static {
		DigesterLoader loader = DigesterLoader.newLoader(new IdentifyDigesterRuleModule());
		digester = loader.newDigester();
	}

	/**
	 * parse the PluginConfig from text.
	 *
	 * @param text
	 *            the string text.
	 * @return created PluginConfig.
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Identify parse(@Nonnull String text) throws IOException, SAXException {
		Reader reader = new StringReader(text);
		return digester.parse(reader);
	}

	/**
	 * parse the PluginConfig from input stream.
	 *
	 * @param is
	 *            input stream.
	 * @return the parsed PluginConfig.
	 * @throws java.io.IOException
	 * @throws org.xml.sax.SAXException
	 */
	public static Identify parse(@Nonnull InputStream is) throws IOException, SAXException {
		return digester.parse(is);
	}
}
