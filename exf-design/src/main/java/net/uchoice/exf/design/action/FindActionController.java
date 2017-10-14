package net.uchoice.exf.design.action;

import java.util.List;
import java.util.stream.Collectors;

import net.uchoice.exf.core.loader.PluginManager;
import net.uchoice.exf.model.action.ActionSpec;
import net.uchoice.exf.model.variable.VariableSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CodeDoge 2017/10/12
 * @version 1.0
 * @since 1.0
 */
@RestController
public class FindActionController {

    @Autowired
    private PluginManager pluginManager;

    @GetMapping("/actions")
    public List<ExfAction> findAllAction() {
        List<ActionSpec> actionSpecs = pluginManager.getPluginCache().getAllActionSpec();
        return actionSpecs.stream().map(actionSpec -> {
            ExfAction action = new ExfAction();
            action.setCode(actionSpec.getCode());
            action.setName(actionSpec.getName());
            action.setDescription(actionSpec.getName());
            action.setFileds(actionSpec.getPropVariables().values().stream().map(this::map).collect(
                Collectors.toList()));
            action.setParameters(actionSpec.getInVariables().values().stream().map(this::map).collect(
                Collectors.toList()));
            return action;
        }).collect(Collectors.toList());
    }

    private ActionVariable map(VariableSpec spec) {
        ActionVariable variable = new ActionVariable();
        variable.setName(spec.getName());
        variable.setDescription(spec.getName());
        variable.setType(spec.getClass());
        variable.setRequired(spec.isRequired());
        return variable;
    }
}
