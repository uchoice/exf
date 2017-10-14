package net.uchoice.exf.design;

import net.uchoice.exf.client.exception.ExfException;
import net.uchoice.exf.core.loader.PluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author CodeDoge 2017/10/14
 * @version 1.0
 * @since 1.0
 */
@Component
@Configuration
public class PluginManagerConfiguration {

    @Bean
    public PluginManager getPluginManager() throws ExfException {
        return PluginManager.getInstance();
    }
}
