package net.uchoice.exf.design.service;

import java.util.List;
import java.util.stream.Collectors;

import net.uchoice.exf.core.config.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CodeDoge 2017/10/12
 * @version 1.0
 * @since 1.0
 */
@RestController
public class FindServiceController {

    @Autowired
    private ServiceManager serviceManager;

    @GetMapping("/services")
    public List<ExfService> findAllAction() {
        return serviceManager.getServiceMap().values().stream().map((service) -> {
            ExfService exfService = new ExfService();
            exfService.setCode(service.getCode());
            exfService.setName(service.getName());
            exfService.setVersion(service.getVersion());
            exfService.setConfigs(service.getConfigs());
            return exfService;
        }).collect(Collectors.toList());
    }

}
