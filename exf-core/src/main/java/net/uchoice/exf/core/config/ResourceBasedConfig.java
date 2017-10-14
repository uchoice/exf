package net.uchoice.exf.core.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import net.uchoice.exf.core.config.parser.ServiceParser;
import net.uchoice.exf.core.matcher.MatcherBuilder;
import net.uchoice.exf.core.matcher.exception.MatchException;
import net.uchoice.exf.model.action.ActionInst;
import net.uchoice.exf.model.container.ContainerInst;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ResourceBasedConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceBasedConfig.class);

    /**
     * 符合exf规则的资源文件
     * FIXME 这个表达式也太宽松了，极易load大量非lbs配置文件
     */
    private static final String RESOURCES_PATH = "exf/service/**/*.xml";

    private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    private ServiceManager configManager = ServiceManager.getInstance();

    public ServiceManager getServiceManager() {
        return configManager;
    }

    /**
     * 初始化
     *
     * @throws IOException
     */
    public void init() throws IOException {
        //获取指定资源路径下的资源
        Resource[] resources = resolver.getResources(RESOURCES_PATH);
        if (null != resources && resources.length > 0) {
            for (Resource resource : resources) {
                try {
                    //从配置文件解析出服务定义
                    Service serviceConfig = ServiceParser.parse(getPluginContextFromURL(resource.getURL()));
                    if (null != serviceConfig) {
                        // 节点匹配器生成
                        generateMatcher(serviceConfig);
                        configManager.addServiceConfig(serviceConfig);
                    }
                } catch (Throwable e) {
                    LOGGER.error(String.format("load service config error: [%s]", resource.getFilename()), e);
                }
            }
        }
    }

    /**
     * 节点匹配器生成
     *
     * @param serviceConfig
     * @throws MatchException
     */
    private void generateMatcher(Service serviceConfig) {
        if (null != serviceConfig && !serviceConfig.getContainers().isEmpty()) {
            for (ContainerInst container : serviceConfig.getContainers()) {
                if (null != container.getActions() && !container.getActions().isEmpty()) {
                    for (ActionInst action : container.getActions()) {
                        if (StringUtils.isNotBlank(action.getMatchExpression())) {
                            action.setMatcher(
                                MatcherBuilder.build(serviceConfig.getMatchers(), action.getMatchExpression()));
                        }
                    }
                }
            }
        }
    }

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
