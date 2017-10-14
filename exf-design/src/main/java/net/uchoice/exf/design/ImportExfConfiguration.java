package net.uchoice.exf.design;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

/**
 * @author CodeDoge 2017/10/15
 * @version 1.0
 * @since 1.0
 */
@Component
@Configuration
@ImportResource("classpath*:/conf/spring-exf.xml")
public class ImportExfConfiguration {
}
