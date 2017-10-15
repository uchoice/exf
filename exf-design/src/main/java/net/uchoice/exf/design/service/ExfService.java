package net.uchoice.exf.design.service;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.uchoice.exf.core.config.ServiceConfig;
import net.uchoice.exf.design.ExfComponent;

/**
 * @author CodeDoge 2017/10/15
 * @version 1.0
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExfService extends ExfComponent {
    private String version;

    private List<ServiceConfig> configs;
}
