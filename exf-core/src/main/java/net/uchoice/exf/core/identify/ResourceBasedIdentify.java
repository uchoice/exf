package net.uchoice.exf.core.identify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import net.uchoice.exf.client.exception.ErrorMessage;
import net.uchoice.exf.client.exception.ExfException;
import net.uchoice.exf.core.config.Service;
import net.uchoice.exf.core.config.ServiceManager;
import net.uchoice.exf.core.identify.parse.IdentifyParser;
import net.uchoice.exf.core.matcher.MatcherBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ResourceBasedIdentify {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceBasedIdentify.class);

	private static final String RESOURCES_PATH = "exf/identify/**/*.xml";

	private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	private IdentifyManager identifyManager = IdentifyManager.getInstance();

	/**
	 * 初始化
	 * @throws IOException
	 */
	public void init() throws IOException {
		Resource[] resources = resolver.getResources(RESOURCES_PATH);
		if (null != resources && resources.length > 0) {
			for (Resource resource : resources) {
				try {
					Identify identify = IdentifyParser.parse(getPluginContextFromURL(resource.getURL()));
					if(identifyManager.contain(identify)){
						throw new ExfException(
								ErrorMessage.code("I-EXF-01-01-005", identify.getId(), resource.getURL().toString()));
					}
					if (null != identify) {
						// 生成识别器
						identify.setMatcher(
								MatcherBuilder.build(identify.getAvailableMatchers(), identify.getMatchExpression()));
						// 绑定身份到对应服务下
						bindIdentifyToService(identify);
						identifyManager.addIdentify(identify);
					}
				} catch (Throwable e) {
					LOGGER.error(String.format("load identify error: [%s]", resource.getURL().toString()), e);
				}
			}
		}
	}

	/**
	 * 身份绑定到服务下
	 * @param identify
	 */
	private void bindIdentifyToService(Identify identify) {
		for(String serviceConfig: identify.getConfigs().keySet()){
			Service service = ServiceManager.getInstance().getServiceMap().get(serviceConfig);
			if(null != service){
				service.addSupportIdentify(identify);
			}
		}
	}

	/**
	 * 身份配置文件读取
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private String getPluginContextFromURL(URL url) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
		String line;
		StringBuilder context = new StringBuilder();
		while ((line = br.readLine()) != null) {
			context.append(line);
		}
		return context.toString();
	}
}
