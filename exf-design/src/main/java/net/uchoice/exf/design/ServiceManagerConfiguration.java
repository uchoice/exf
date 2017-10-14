package net.uchoice.exf.design;

import java.io.IOException;

import net.uchoice.exf.core.config.ResourceBasedConfig;
import net.uchoice.exf.core.config.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author CodeDoge 2017/10/15
 * @version 1.0
 * @since 1.0
 */
@Component
@Configuration
public class ServiceManagerConfiguration {

    private final ResourceBasedConfig resourceBasedConfig;

    @Autowired
    public ServiceManagerConfiguration(ResourceBasedConfig resourceBasedConfig) {
        this.resourceBasedConfig = resourceBasedConfig;
    }

    @Bean
    public ServiceManager getServiceManager() throws IOException {
        return resourceBasedConfig.getServiceManager();
    }
}
