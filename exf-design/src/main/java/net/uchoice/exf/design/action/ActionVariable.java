package net.uchoice.exf.design.action;

import lombok.Data;

/**
 * @author CodeDoge 2017/10/12
 * @version 1.0
 * @since 1.0
 */
@Data
public class ActionVariable {
    private String name;
    private String description;
    private boolean required;
    private Class type;
    private Object defaultValue;
}
