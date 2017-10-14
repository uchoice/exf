package net.uchoice.exf.design.container;

import java.util.List;
import java.util.stream.Collectors;

import net.uchoice.exf.core.loader.PluginManager;
import net.uchoice.exf.model.container.ContainerSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CodeDoge 2017/10/12
 * @version 1.0
 * @since 1.0
 */
@RestController
public class FindContainerController {

    @Autowired
    private PluginManager pluginManager;

    @GetMapping("/containers")
    public List<ExfContainer> findAllAction() {
        List<ContainerSpec> actionSpecs = pluginManager.getPluginCache().getAllContainerSpec();
        return actionSpecs.stream().map(actionSpec -> {
            ExfContainer container = new ExfContainer();
            container.setCode(actionSpec.getCode());
            container.setName(actionSpec.getName());
            container.setDescription(actionSpec.getName());
            return container;
        }).collect(Collectors.toList());
    }

}
